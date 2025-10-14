package com.test.test.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.test.test.enumerate.SexEnum;
import com.test.test.enumerate.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentUserVO {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "邮件id")
    private String id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "加密后的密码")
    private String password;

    @Schema(description = "盐")
    private String salt;

    @Schema(description = "邮箱")
    private String mail;

    @Schema(description = "手机号")
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
