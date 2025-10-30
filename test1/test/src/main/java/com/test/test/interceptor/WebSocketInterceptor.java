package com.test.test.interceptor;


import com.alibaba.fastjson2.JSON;
import com.test.test.bo.UserLoginData;
import com.test.test.redis.RedisPrefix;
import com.test.test.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketInterceptor implements HandshakeInterceptor {


    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String token = request.getURI().getQuery().split("token=")[1];
        //判断token是否为空
        if (token == null || token.isEmpty()) {
            log.info("未登录的请求");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        Long userId;
        Claims claims;
        try {
            //2、校验令牌
            //根据Key解密token
             claims = JwtUtil.parseJWT(jwtSecretKey, token);
             userId = Long.valueOf(claims.get("id").toString());
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        log.info("用户id:{},建立WebSocket连接握手通过", userId);
        attributes.put("userId", userId);
        System.out.println(attributes.get("userId"));
        response.setStatusCode(HttpStatus.OK);
        return true;
    }

    /**
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}