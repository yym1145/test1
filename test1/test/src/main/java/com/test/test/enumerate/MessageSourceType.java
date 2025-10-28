package com.test.test.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Illya
 * @Date: 2024/8/25 15:57
 */
@AllArgsConstructor
@Getter
public enum MessageSourceType {
    
    SYSTEM(0, "系统"),
    USER(1, "用户");

    @EnumValue
    final Integer code;

    @JsonValue
    final String name;
}
