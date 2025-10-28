package com.test.test.util;

import com.google.code.kaptcha.Producer;
import com.test.test.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class CaptchaUtil {

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 验证码过期时间（分钟）
    private static final int EXPIRE_MINUTES = 5;

    /**
     * 生成验证码图片并存储到Redis
     * @return 验证码唯一标识（用于后续验证）
     */
    public String generateCaptcha(HttpServletResponse response) throws IOException {
        // 生成验证码文本
        String code = kaptchaProducer.createText();
        
        // 生成唯一标识（UUID）
        String captchaKey = "captcha:" + UUID.randomUUID().toString();
        
        // 存储到Redis（设置过期时间）
        redisTemplate.opsForValue().set(captchaKey, code, EXPIRE_MINUTES, TimeUnit.MINUTES);
        
        // 生成验证码图片并写入响应
        BufferedImage image = kaptchaProducer.createImage(code);
        response.setContentType("image/jpeg");
        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(image, "jpg", out);
        }
        
        return captchaKey; // 返回标识给前端
    }

    /**
     * 验证验证码
     * @param captcha 验证码标识
     * @param inputCode 用户输入的验证码
     * @return 验证结果
     */
    public boolean validateCaptcha(String captcha, String inputCode) {
        if (captcha == null || inputCode == null) {
            return false;
        }
        
        // 从Redis获取验证码
        String code = redisTemplate.opsForValue().get("captcha:" + captcha);
        if (code == null) {
            throw new BaseException("验证码已过期或不存在"); // 验证码已过期或不存在
        }
        
        // 验证通过后删除验证码（防止重复使用）
        boolean result = code.equalsIgnoreCase(inputCode);
        if (result) {
            redisTemplate.delete("captcha:" + captcha);
        }
        return result;
    }
}