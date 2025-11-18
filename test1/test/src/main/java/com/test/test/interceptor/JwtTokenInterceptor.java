package com.test.test.interceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.test.test.bo.UserLoginData;
import com.test.test.exception.BaseException;
import com.test.test.redis.RedisPrefix;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 *
 * jwt令牌校验的拦截器
 * 身份验证，权限验证
 */
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, String> redisTemplate;

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
            String s = redisTemplate.opsForValue().get(RedisPrefix.USER_LOGIN_DATA.getPrefix() + userId);
            if (s == null){
                throw new BaseException("请重新登录");
            }
            UserLoginData userLoginData = objectMapper.readValue(s, UserLoginData.class);
            // 4. 关键判断：当前请求的 Token 是否与 Redis 中存储的 Token 一致
            if (!token.equals(userLoginData.getToken())) {
                // Token 不一致，说明账号在其他设备登录过，旧 Token 失效
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(objectMapper.writeValueAsString(Result.error("账号已在其他设备登录，请重新登录")));
                response.getWriter().flush();
                return false;
            }
            List<Long> roleIds = userLoginData.getRoleIds();
            BaseContext.setCurrentUserRoleIds(roleIds);
            String requestPath = request.getRequestURI();
            //获取基本权限
            List<String> basicPermission = objectMapper.readValue(redisTemplate.opsForValue().get(RedisPrefix.ROLE_BASIC_PERMISSION.getPrefix()), new TypeReference<ArrayList<String>>() {});
            if (basicPermission != null && !basicPermission.isEmpty()) {
                for (String path : basicPermission) {
                    //使用正则表达式匹配权限
                    if (Pattern.matches(path.replaceAll("/+$","") + "(/.*)?", requestPath)) {
                        //匹配成功,放行
                        response.setStatus(HttpStatus.OK.value());
                        return true;
                    }
                }
            }

            if (roleIds != null && !roleIds.isEmpty()) {
                for (Long id : roleIds) {
                    //获取用户对应角色权限
                    List<String> rolePermission = objectMapper.readValue(redisTemplate.opsForValue()
                                    .get(RedisPrefix.ROLE_DATA_PERMISSION.getPrefix() + id)
                            , new TypeReference<List<String>>() {});
                    // 判断所拥有的权限与访问目标是否匹配
                    if (rolePermission != null && !rolePermission.isEmpty()) {
                        for (String path : rolePermission) {
                            //使用正则表达式匹配权限
                            if (Pattern.matches(path.replaceAll("/+$","") + "(/.*)?", requestPath)) {
                                //匹配成功,放行
                                response.setStatus(HttpStatus.OK.value());
                                return true;
                            }
                        }
                    }
                }
            }

            //未匹配到权限
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("无权限访问资源")));
            response.getWriter().flush();
            return false;
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
//        return true;
    }
}