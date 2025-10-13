package com.test.test.redis;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisPrefix {

    /**
     * 用户-用户登陆数据
     */
    USER_LOGIN_DATA(1, "用户登录数据", "user:login:data:"),
    ROLE_DATA(100,"角色数据","role:data:"),
    ROLE_DATA_MENU(101,"角色数据-菜单","role:data:menu:"),
    ROLE_DATA_PERMISSION(102,"角色数据-权限","role:data:permission:"),
    ROLE_BASIC_PERMISSION(103,"角色数据-基础权限","role:data:basic:permission"),;

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String desc;

    private final String prefix;
}
