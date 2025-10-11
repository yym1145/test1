package com.test.test.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.test.context.BaseContext;
import com.test.test.result.Result;
import com.test.test.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * @author Refjttria
 * jwt令牌校验的拦截器
 * 身份验证，权限验证
 */
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {


    private final ObjectMapper objectMapper;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    /**
     * 方法执行前运行
     *
     * @param request   请求
     * @param response  响应
     * @param handler   请求头
     * @return  是否放行
     * @throws IOException  IO异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "token");
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        //1、从请求头中获取令牌
        String token = request.getHeader("token");
        //判断token是否为空
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("token为空")));
            response.getWriter().flush();
            return false;
        }
        try {
            //2、校验令牌
            //根据Key解密token
            Claims claims = JwtUtil.parseJWT(jwtSecretKey, token);
            Long userId = Long.valueOf(claims.get("id").toString());
            //将ID存入线程空间中
            BaseContext.setCurrentUserId(userId);
        } catch (Exception e) {
            if( e.getClass() == ExpiredJwtException.class ){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(objectMapper.writeValueAsString(Result.error("token过期")));
                response.getWriter().flush();
                return false;
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("token无效")));
            response.getWriter().flush();
            return false;
        }
        return true;
    }
}