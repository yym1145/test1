package com.test.test.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {

    SYSTEM_NOTIFY(0, "系统通知"),
    CHAT(1, "聊天"),
    COMMENT_REPLY(2, "评论回复"),
    LIKES_RECEIVED(3, "获得的赞");

    @EnumValue
    final Integer code;
    
    @JsonValue
    final String name;
}
