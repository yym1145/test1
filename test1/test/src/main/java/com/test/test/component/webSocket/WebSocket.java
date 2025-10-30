package com.test.test.component.webSocket;


import com.alibaba.fastjson2.JSON;
import com.test.test.context.BaseContext;
import com.test.test.exception.BaseException;
import com.test.test.mongoDB.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocket extends TextWebSocketHandler {

    //连接数
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    //在线用户
    private static Map<Long, WebSocketSession> clients = new ConcurrentHashMap<>();

    /**
     * socket 建立成功事件 @OnOpen
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        //保存用户ID对应连接
        if (session.getAttributes().get("userId") != null) {
            clients.put((Long) session.getAttributes().get("userId"), session);
        }
        //连接数加1
        onlineCount.incrementAndGet();
    }

    /**
     * 接收消息事件 @OnMessage
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    /**
     * socket 断开连接时 @OnClose
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        clients.remove((Long) session.getAttributes().get("userId"));
        onlineCount.decrementAndGet();
    }

    public void systemNotify(Long targetUserId, String message) {
        systemNotify(List.of(targetUserId), message);
    }

    public void systemNotify(List<Long> targetUserId, String message) {

    }

    //发送消息
    public void send(Message message) throws IOException {
        //获取用户目标用户连接
        WebSocketSession session = clients.get(message.getTargetUserId());
        if (session == null) {
            //若目标用户不在线,则不发送消息
            throw new BaseException("用户不在线");
        }
        //否则发送实时通知
        session.sendMessage(new TextMessage(JSON.toJSONString(message)));
    }

    public void close() {
        close(BaseContext.getCurrentUserId());
    }

    //主动断开连接
    public void close(Long userId) {
        try {
            //获取断开连接信息
            WebSocketSession session = clients.remove(userId);
            onlineCount.decrementAndGet();
            if (session != null) {
                //清除连接
                session.close();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    //获取连接数
    public Integer getOnlineCount() {
        return onlineCount.get();
    }
}