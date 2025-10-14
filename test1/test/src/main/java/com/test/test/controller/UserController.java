package com.test.test.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.test.test.dto.user.*;
import com.test.test.entiy.User;
import com.test.test.result.PageResult;
import com.test.test.result.Result;
import com.test.test.service.UserService;
import com.test.test.vo.role.menu.MenuVO;
import com.test.test.vo.user.CurrentUserVO;
import com.test.test.vo.user.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理")
public class UserController {

    private final UserService userService;

    /**
     * 新增用户
     *
     * @param  dto
     * @return
     */
    @PostMapping("/addUser")
    @Operation(summary = "新增用户")
    public Result<String> addUser(@Validated @RequestBody AddUserDTO dto){
        return Result.success("新增成功",userService.addUser(dto));
    }

    /**
     * 批量删除用户
     *
     * @param  dto
     * @return
     */
    @PostMapping("/deleteUser")
    @Operation(summary = "批量删除用户")
    public Result deleteUser(@Validated@RequestBody DeleteUserDTO dto){
         return userService.deleteUser(dto.getIdlist());
    }

    /**
     * 登录
     *
     * @param  dto
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result<UserLoginVO> login(@Validated @RequestBody UserLoginDTO dto) throws JsonProcessingException {
        return Result.success("登陆成功",userService.login(dto));
    }

    /**
     * 发送验证码
     *
     * @param  dto
     * @return
     */
    @PostMapping("/sendVerificationCode")
    @Operation(summary = "发送验证码")
    public Result sendVerificationCode(@Validated @RequestBody SendVerificationCodeDTO dto){
        return userService.sendVerificationCode(dto);
    }

    /**
     * 验证码验证
     *
     * @param  dto
     * @return
     */
    @PostMapping("/verificationCodeValidation")
    @Operation(summary = "验证码验证")
    public Result verificationCodeValidation(@RequestBody VerificationCodeValidationDTO dto){
        return userService.verificationCodeValidation(dto);
    }

    /**
     * 忘记密码
     *
     * @param  dto
     * @return
     */
    @PostMapping("/forgetPassword")
    @Operation(summary = "忘记密码")
    public Result forgetPassword(@RequestBody ForgetPasswordDTO dto){
        return userService.forgetPassword(dto);
    }

    /**
     * 修改密码
     *
     * @param  dto
     * @return
     */
    @PostMapping("/updatePassword")
    @Operation(summary = "修改密码")
    public Result updatePassword(@RequestBody UpdatePasswordDTO dto){
        return userService.updatePassword(dto);
    }

    /**
     * 分页查询用户
     *
     * @param  dto
     * @return
     */
    @PostMapping("/pageUser")
    @Operation(summary = "分页查询用户")
    public Result<PageResult<User>>pageUser(@Validated@RequestBody PageUserDTO dto){
        PageResult<User> pageResult=userService.pageUser(dto);
        return Result.success("查询成功",pageResult);
    }

    /**
     * 修改用户
     *
     * @param  updateUserDTO
     * @return
     */
    @PostMapping("/updateUser")
    @Operation(summary = "修改用户")
    public Result updateUser(@Validated@RequestBody UpdateUserDTO updateUserDTO){
        return userService.updateUser(updateUserDTO);
    }



    /**
     * 修改用户
     *
     * @param  id
     * @return
     */
    @PostMapping("/selectOneUser")
    @Operation(summary = "单个用户查询")
    public Result<User> selectOneUser(@Valid @RequestParam(value = "id",required = true)
                                          @Schema(description = "用户id")Long id){
        User user=userService.selectOneUser(id);
        return Result.success("查询成功",user);
    }

    @GetMapping("/getCurrentUserInformation")
    @Operation(summary = "获取当前用户的基本信息")
    public Result<CurrentUserVO>getCurrentUserInformation(){
        CurrentUserVO vo=userService.getCurrentUserInformation();
        return Result.success("查询成功",vo);
    }

    @GetMapping("/getMenu")
    @Operation(summary = "获取菜单")
    public Result<List<MenuVO>> getMenu() throws JsonProcessingException {
        return Result.success("查询成功", userService.getMenu());
    }
}
