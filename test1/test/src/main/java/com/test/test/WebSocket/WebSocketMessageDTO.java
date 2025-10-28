package com.test.test.WebSocket;



import com.test.test.WebSocket.enumeration.OperationType;
import lombok.Data;

/**
 * @Author: Illya
 * @Date: 2024/9/3 下午4:36
 */
@Data
public class WebSocketMessageDTO {
    //操作类型枚举(0,获取消息,1,删除消息,2,发送消息)
    private OperationType operationType;

    //需要删除的消息的ID
    private String deleteMessageID;

    //发送信息目的地用户ID
    private Long targetUserId;

    //需要发送的消息内容
    private String sendMessage;
}
