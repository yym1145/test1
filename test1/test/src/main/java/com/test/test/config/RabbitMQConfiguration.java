package com.test.test.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@EnableRabbit
@RequiredArgsConstructor
@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        // 信任目标类所在包（或直接指定类）
        typeMapper.setTrustedPackages("com.test.test.mongoDB");
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setJavaTypeMapper(typeMapper);
        converter.setDefaultCharset(StandardCharsets.UTF_8.name());
        return converter;
    }

}
