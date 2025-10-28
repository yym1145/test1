package com.test.test.component.rabbitMQ;

import com.test.test.component.webSocket.WebSocket;
import com.test.test.mongoDB.Message;
import com.test.test.service.MessageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQ {

    private final WebSocket webSocket;

    private final MessageService messageService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "error.queue"),
            exchange = @Exchange(value = "error.direct"),
            key = "error"
    ))
    public void errorListener(String msg) {
        log.error(msg);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "online.message"),
            exchange = @Exchange(value = "message.direct"),
            key = "online"
    ))
    public void onlineListener(Message message) {
        try {
            webSocket.send(message);
            messageService.lastMessage(List.of(message.getId()));
        } catch (Exception e) {
            log.error("消息发送失败,原因:{}", e.getMessage());
        }
    }
}
