package com.test.test.WebSocket.enumeration;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperationType {

    GET_MESSAGE(0, "获取消息"),
    DELETE_MESSAGE(1, "删除消息"),
    SEND_MESSAGE(2, "发送消息");

    @EnumValue
    @JsonValue
    private final Integer code;
    
    private final String name;

}
