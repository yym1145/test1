package com.test.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.test.bo.UserLoginData;
import com.test.test.bo.UserLoginVerifyData;
import com.test.test.context.BaseContext;
import com.test.test.dto.user.*;
import com.test.test.entiy.User;
import com.test.test.enumerate.StatusEnum;
import com.test.test.exception.BaseException;
import com.test.test.mapper.UserMapper;
import com.test.test.redis.RedisPrefix;
import com.test.test.result.PageResult;
import com.test.test.result.Result;
import com.test.test.service.UserService;
import com.test.test.util.CodeUtil;
import com.test.test.util.JwtUtil;
import com.test.test.util.SaltUtil;
import com.test.test.vo.role.menu.MenuVO;
import com.test.test.vo.user.CurrentUserVO;
import com.test.test.vo.user.UserLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    // 保存收件人邮箱
    private String mail;

    /**
     * 新增用户
     *
     * @param  addUserDTO
     * @return
     */
    @Override
    public String addUser(AddUserDTO addUserDTO) {
        System.out.println(BaseContext.getCurrentUserRoleIds());
        //创建用户
        User user = new User();
        BeanUtils.copyProperties(addUserDTO,user);
        user.setId(String.valueOf(IdWorker.getId()));
        user.setStatusEnum(StatusEnum.START);

        //判断邮箱是否存在
        QueryWrapper<User>mailQueryWrapper=new QueryWrapper<>();
        mailQueryWrapper.eq("mail",addUserDTO.getMail());
        Long mailCount=userMapper.selectCount(mailQueryWrapper);
        if (mailCount!=0){
            throw new BaseException("邮箱已存在");
        }


        //判断邮箱是否存在
        QueryWrapper<User>userNameQueryWrapper=new QueryWrapper<>();
        userNameQueryWrapper.eq("user_name",addUserDTO.getUserName());
        Long userNameCount=userMapper.selectCount(userNameQueryWrapper);
        if (userNameCount!=0){
            throw new BaseException("用户名已存在");
        }


        if (addUserDTO.getPassword()==null){
            String salt= SaltUtil.generateSalt(16);
            user.setSalt(salt);
            String password="123456"+salt;
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        }else {
            //生成盐值和加密密码
            String salt= SaltUtil.generateSalt(16);
            user.setSalt(salt);
            String password=addUserDTO.getPassword()+salt;
            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        }
        if (userMapper.insert(user)!=1){
            throw new BaseException("新增失败");
        }
        return user.getId().toString();
    }

    /**
     * 批量删除用户
     *
     * @param  ids
     * @return
     */
    @Override
    public Result deleteUser(List<String> ids) {
        List<User>userList=userMapper.selectBatchIds(ids);

        if (ids.contains(BaseContext.getCurrentUserId().toString())){
            return Result.error("不能删除自己");
        }

        //判断删除的邮件历史记录id是否存在
        if (ids.size()== userList.size()){
            userMapper.deleteBatchIds(ids);
            return Result.success("删除成功",null);
        }else {
            return Result.error("删除失败，用户不存在");
        }
    }

    /**
     * 登录
     *
     * @param  dto
     * @return
     */
    @Override
    public UserLoginVO login(UserLoginDTO dto) throws JsonProcessingException {
        UserLoginVerifyData user = userMapper.getUserLoginDataByAccount(dto.getMail());
        if (user == null){
            throw new BaseException("用户不存在");
        }
        // 创建用户登录验证数据对象，用于处理登录验证相关信息
        UserLoginVerifyData data=new UserLoginVerifyData();
        BeanUtils.copyProperties(user,data);
        // 构建待加密的密码字符串：用户输入的密码 + 数据库中存储的盐值
        String password=dto.getPassword()+data.getSalt();
        // 对密码进行MD5加密处理
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        // 验证加密后的密码与数据库中存储的密码是否一致
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new BaseException("密码错误");
        }
        // 创建JWT声明（claims）对象，用于存储自定义负载信息
        Map<String, Object> claims = new HashMap<>();
        // 将用户ID存入声明中，用于生成令牌
        claims.put("id", data.getId());
        //生成token
        String token = JwtUtil.createJWT(
                jwtSecretKey,
                jwtExpiration * 3600 * 1000,
                claims);
        //返回用户信息
        UserLoginData userLoginData = new UserLoginData();
        userLoginData.setId(user.getId());
        userLoginData.setToken(token);
        userLoginData.setRoleIds(user.getRoleIds());
        redisTemplate.opsForValue().set(RedisPrefix.USER_LOGIN_DATA.getPrefix() + user.getId(), objectMapper.writeValueAsString(userLoginData), jwtExpiration, TimeUnit.HOURS);
        return UserLoginVO
                .builder()
                .id(data.getId())
                .userName(data.getUserName())
                .token(token).build();
    }

    @Override
    public Result sendVerificationCode(SendVerificationCodeDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 生成随机验证码
        String code = CodeUtil.generateCode(6);
        // 构建邮件内容，包含验证码信息和提示
        String text = "您的验证码为：" + code + ",请勿泄露给他人。";
        // 设置邮件发送者（发件人邮箱
        message.setFrom(sendMailer);
        // 设置邮件接收者
        message.setTo(dto.getMail());
        // 设置邮件正文内容
        message.setText(text);
        // 设置邮件发送时间为当前时间
        message.setSentDate(new Date());
        // 设置邮件主题
        message.setSubject("登录验证码");
        // 保存收件人邮箱
        mail=dto.getMail();
        try {
            javaMailSender.send(message);
            redisTemplate.opsForValue().set("code:" + mail, String.valueOf(code), Duration.ofMinutes(5));
            return Result.success("发送成功",null);

        }catch (Exception e){
            return Result.error("发送失败");
        }

    }
    /**
     * 忘记密码
     *
     * @param dto
     */
    @Override
    public void forgetPassword(ForgetPasswordDTO dto) {
        String key = dto.getMail() != null ? dto.getMail() : dto.getMobile();
        if (CodeUtil.checkCode(key, dto.getCode())) {
            QueryWrapper<User>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("mail",dto.getMail());
            User oldUser=userMapper.selectOne(queryWrapper);
            //判断用户是否存在，不存在则返回错误信息
            if (oldUser==null){
                throw new BaseException("用户不存在");
            }
            String salt = SaltUtil.generateSalt(16);
            oldUser.setSalt(salt);
            String newPassword = dto.getNewPassword() + salt;
            oldUser.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
            User newUser = new User();
            BeanUtils.copyProperties(oldUser,newUser);
            userMapper.updateById(newUser);
        }
    }

    /**
     * 修改密码
     *
     * @param  dto
     * @return
     */
    @Override
    public Result updatePassword(UpdatePasswordDTO dto) {
        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        // 设置查询条件：根据邮箱查询用户
        queryWrapper.eq("mail",dto.getMail());
        //  根据邮箱查询用户信息
        User oldUser=userMapper.selectOne(queryWrapper);
        //判断用户是否存在，不存在则返回错误信息
        if (oldUser==null){
            return Result.error("用户不存在");
        }
        //生成16位随机盐值，用于密码加密
        String salt = SaltUtil.generateSalt(16);
        //将盐值设置到用户对象中
        oldUser.setSalt(salt);
        //构建新密码：新密码明文 + 盐值
        String newPassword = dto.getNewPassword() + salt;
        //对新密码进行MD5加密处理，并设置到用户对象中
        oldUser.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        User newUser = new User();
        BeanUtils.copyProperties(oldUser,newUser);
        userMapper.updateById(newUser);
        return Result.success("修改成功 ",null);
    }

    /**
     * 分页查询邮件历史记录
     *
     * @param  dto
     * @return
     */
    @Override
    public PageResult<User> pageUser(PageUserDTO dto) {
        // 1. 创建分页对象，设置当前页码和每页显示条数
        Page<User>page=new Page<>(dto.getPage(),dto.getPageSize());
        // 2. 创建查询条件构造器，用于构建SQL查询条件
        LambdaQueryWrapper<User>queryWrapper=new LambdaQueryWrapper<>();
        // 3. 构建模糊查询条件：用户名不为空时，添加用户名模糊查询
        queryWrapper.like(StringUtils.isNotBlank(dto.getUserName()),
                User::getUserName,dto.getUserName());
        // 4. 构建模糊查询条件：邮箱不为空时，添加邮箱模糊查询
        queryWrapper.like(StringUtils.isNotBlank(dto.getMail()),
                User::getMail,dto.getMail());
        // 5. 构建模糊查询条件：手机号不为空时，添加手机号模糊查询
        queryWrapper.like(StringUtils.isNotBlank(dto.getMobile()),
                User::getMobile,dto.getMobile());
        // 6. 构建模糊查询条件：地址不为空时，添加地址模糊查询
        queryWrapper.like(StringUtils.isNotBlank(dto.getAddress()),
                User::getAddress,dto.getAddress());
        // 7. 构建精确查询条件：性别不为空时，添加性别精确匹配
        queryWrapper.eq(dto.getSexEnum()!=null, User::getSexEnum,dto.getSexEnum());
        // 8. 构建精确查询条件：状态不为空时，添加状态精确匹配
        queryWrapper.eq(dto.getStatusEnum()!=null, User::getStatusEnum,dto.getStatusEnum());
        // 9. 调用mapper层方法执行分页查询，获取查询结果
        Page<User>result=userMapper.selectPage(page,queryWrapper);

        return new PageResult<>(result.getTotal(),result.getRecords());
    }

    /**
     * 修改用户
     *
     * @param  dto
     * @return
     */
    @Override
    public Result updateUser(UpdateUserDTO dto) {
        QueryWrapper<User>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",dto.getId());
        User oldUser=userMapper.selectOne(queryWrapper);
        List<String>mailList=userMapper.mailList();
        if (oldUser==null){
            throw new BaseException("用户不存在");
        }
        if (mailList.contains(dto.getMail())){
            throw new BaseException("邮箱已存在");
        }
        User user=new User();
        BeanUtils.copyProperties(dto,user);
        //生成16位随机盐值，用于密码加密
        String salt = SaltUtil.generateSalt(16);
        //将盐值设置到用户对象中
        user.setSalt(salt);
        //构建新密码：新密码明文 + 盐值
        String newPassword = dto.getPassword() + salt;
        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        userMapper.updateUser(user);
        return Result.success("修改成功",null);
    }

    @Override
    public User selectOneUser(Long id) {
        User user=userMapper.selectById(id);
        if (user==null){
            throw new BaseException("用户不存在");
        }
        return user;
    }

    @Override
    public CurrentUserVO getCurrentUserInformation() {
        CurrentUserVO vo=userMapper.getCurrentUserInformation(BaseContext.getCurrentUserId());
        return vo;
    }

    @Override
    public List<MenuVO> getMenu() throws JsonProcessingException {
        List<Long> roleIds = BaseContext.getCurrentUserRoleIds();
        Map<Long, MenuVO> menuMap = new HashMap<>();
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long id : roleIds) {
                String menuJson = redisTemplate.opsForValue().get(RedisPrefix.ROLE_DATA_MENU.getPrefix() + id);
                if (menuJson != null && !menuJson.isEmpty()){
                    List<MenuVO> menus = objectMapper.readValue(menuJson,new TypeReference<List<MenuVO>>() {});
                    for (MenuVO menu : menus) {
                        if (menu != null && !menuMap.containsKey(menu.getId())) {
                            menuMap.put(menu.getId(), menu);
                        }
                    }
                }
            }
        }
        //创建根菜单列表
        List<MenuVO> rootMenuList = new ArrayList<>();
        //第一次遍历,构建映射,和根菜单,初始化属性
        for (MenuVO menu : menuMap.values()) {
            if (menu.getParentId() == null) {
                // 根节点
                rootMenuList.add(menu);
            } else {
                MenuVO parentMenu = menuMap.get(menu.getParentId());
                if (parentMenu != null) {
                    if (parentMenu.getChildren() == null) {
                        parentMenu.setChildren(new ArrayList<>());
                    }
                    parentMenu.getChildren().add(menu);
                }
            }
        }
        return rootMenuList;
    }
}
