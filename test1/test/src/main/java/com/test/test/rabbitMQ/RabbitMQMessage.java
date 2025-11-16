package com.test.test.rabbitMQ;

import lombok.Data;


@Data
public class RabbitMQMessage<T> {

    private String messageId;

    private String sendUserId;

}
