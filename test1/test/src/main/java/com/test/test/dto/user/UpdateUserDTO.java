package com.test.test.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.test.test.enumerate.SexEnum;
import com.test.test.enumerate.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserDTO {
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户id")
    private String id;

    @Schema(description = "密码")
    @Size(min = 6, max = 32, message = "请输入6~32位密码")
    private String password;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "邮箱")
    private String mail;

    @Schema(description = "手机号")
    @Size(max = 11, message = "联系电话长度超过限制")
    private String mobile;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "性别枚举对象WOMAN女，MAN男")
    private SexEnum sexEnum;

    @Schema(description = "出生年月")
    private Date birthday;

    @Schema(description = "账号状态枚举")
    private StatusEnum statusEnum;

}
