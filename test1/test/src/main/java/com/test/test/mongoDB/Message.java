package com.test.test.mongoDB;
import com.test.test.enumerate.MessageSourceType;
import com.test.test.enumerate.MessageType;
import com.test.test.special.SendUserData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Illya
 * @Date: 2024/9/2 下午2:58
 */
@Data
@Document("message")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message<T>  {
    @Id
    @Schema(description = "消息ID")
    private String id;

    @Schema(description = "消息来源类型(系统,用户)")
    private MessageSourceType messageSourceType;

    @Schema(description = "消息类型")
    private MessageType messageType;

    @Schema(description = "发送用户信息")
    private SendUserData sendUserData;

    @Schema(description = "目标用户ID(消息需要送达的用户ID)")
    private Long targetUserId;

    @Schema(description = "消息数据(根据消息类型的不通会有不同的数据)")
    private T messageData;

    @Schema(description = "发送时间(消息发出的时间)")
    private Date sendTime;

    @Schema(description = "已读状态(false未读,true已读)")
    private Boolean readStatus = false;

    @Schema(description = "是否为新消息(false否,true是,第一次接收到时为true,后变为false)")
    private Boolean lastStatus = true;
}
