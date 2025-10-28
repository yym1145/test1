package com.test.test.rabbitMQ;

import lombok.Data;

/**
 * @Author: Illya
 * @Date: 2024/9/3 下午12:07
 */
@Data
public class RabbitMQMessage<T> {

    private String messageId;

    private String sendUserId;

}
