package com.test.test.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.test.dto.user.*;
import com.test.test.entiy.User;
import com.test.test.result.PageResult;
import com.test.test.result.Result;
import com.test.test.vo.UserLoginVO;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 新增用户
     *
     * @param  addUserDTO
     * @return
     */
    String addUser(AddUserDTO addUserDTO);

    /**
     * 批量删除用户
     *
     * @param  ids
     * @return
     */
    Result deleteUser(List<Long>ids);

    /**
     * 登录
     *
     * @param  dto
     * @return
     */
    UserLoginVO login(UserLoginDTO dto) throws JsonProcessingException;

    /**
     * 发送验证码
     *
     * @param  dto
     * @return
     */
    Result sendVerificationCode(SendVerificationCodeDTO dto);

    /**
     * 验证码验证
     *
     * @param  dto
     * @return
     */
    Result  verificationCodeValidation(VerificationCodeValidationDTO dto);

    /**
     * 忘记密码
     *
     * @param  dto
     * @return
     */
    Result  forgetPassword(ForgetPasswordDTO dto);

    /**
     * 修改密码
     *
     * @param  dto
     * @return
     */
    Result updatePassword(UpdatePasswordDTO dto);

    /**
     * 分页查询邮件历史记录
     *
     * @param  dto
     * @return
     */
    PageResult<User> pageUser(PageUserDTO dto);

    /**
     * 修改用户
     *
     * @param  dto
     * @return
     */
    Result updateUser(UpdateUserDTO dto);

    User selectOneUser(Long id);
}
