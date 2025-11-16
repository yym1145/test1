package com.test.test.special;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendUserData  {
    @Schema(description = "消息来源用户ID,如果为系统消息则没有该字段")
    private Long sendUserId;

    @Schema(description = "消息来源用户姓名,如果为系统消息则没有该字段")
    private String sendUserName;
    
    @Schema(description = "消息来源用户头像(发送消息时),如果为系统消息则没有该字段")
    private String sendUserFace;
}
