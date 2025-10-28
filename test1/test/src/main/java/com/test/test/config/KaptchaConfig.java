package com.test.test.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha kaptchaProducer() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        
        // 验证码配置
        properties.setProperty("kaptcha.border", "yes");          // 是否有边框
        properties.setProperty("kaptcha.border.color", "105,179,90"); // 边框颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue"); // 文本颜色
        properties.setProperty("kaptcha.image.width", "120");     // 图片宽度
        properties.setProperty("kaptcha.image.height", "40");     // 图片高度
        properties.setProperty("kaptcha.textproducer.font.size", "30"); // 字体大小
        properties.setProperty("kaptcha.session.key", "code");    // session键名（此处不用，用Redis）
        properties.setProperty("kaptcha.textproducer.char.length", "4"); // 验证码长度
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑"); // 字体
        
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}