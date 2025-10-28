package com.test.test.config;


import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SwaggerConfig {
    static {
        // 告诉 SpringDoc：忽略 HttpServletResponse 类型的参数
        SpringDocUtils.getConfig()
            .addRequestWrapperToIgnore(HttpServletResponse.class);
    }
}