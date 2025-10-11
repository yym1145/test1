package com.test.test.dto.user;



import com.test.test.enumerate.SexEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class AddUserDTO {
    @Schema(description = "用户名")
    @NotBlank(message = "用户名称为空")
    private String userName;

    @Schema(description = "加密后的密码")
    @Size(min = 6, max = 32, message = "请输入8~32位密码")
    private String password;

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String mail;

    @Schema(description = "手机号")
    @NotBlank(message = "联系电话为空")
    @Size(max = 11, message = "联系电话长度超过限制")
    private String mobile;

    @Schema(description = "地址")
    @NotBlank(message = "地址不能为空")
    private String address;


    @Schema(description = "性别枚举对象WOMAN女，MAN男")
    @NotNull(message = "性别不能为空")
    private SexEnum sexEnum;

    @Schema(description = "出生年月")
    private Date birthday;


}
