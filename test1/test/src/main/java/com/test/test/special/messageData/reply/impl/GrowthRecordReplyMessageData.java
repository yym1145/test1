package com.test.test.special.messageData.reply.impl;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test.test.enumerate.AssociatedContent;
import com.test.test.special.messageData.reply.ReplyMessageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Illya
 * @Date: 2024/8/26 08:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrowthRecordReplyMessageData implements Serializable {

    private AssociatedContent associatedContent = AssociatedContent.GROWTH_RECORD;

    private String msg = "对我的成长记录发表了评论";
    //对应成长记录Id
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private Long growthRecordId;

    //对应的回复ID
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private Long commentId;

    //对应回复的内容
    private String commentMessage;
}
