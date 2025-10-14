package com.test.test.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserLoginDTO {

    @Schema(description = "邮箱",required = true)
    @NotBlank(message = "邮箱不能为空")
    private String mail;

    @Schema(description = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

}
