package com.test.test.controller;


import com.test.test.util.CaptchaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/captcha")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "图形验证码")
public class CaptchaController {

    @Autowired
    private CaptchaUtil captchaUtil;

    /**
     * 生成验证码图片
     * 前端调用后，会返回验证码标识（captchaKey），同时响应图片流
     */
    @GetMapping("/generate")
    @Operation(summary = "生成验证码图片")
    public Map<String, String> generateCaptcha(@ApiIgnore HttpServletResponse response) throws IOException {
        String captchaKey = captchaUtil.generateCaptcha(response);
        Map<String, String> result = new HashMap<>();
        result.put("captchaKey", captchaKey); // 前端需要保存此key用于后续验证
        return result;
    }

    /**
     * 验证验证码
     * @param captchaKey 验证码标识
     * @param inputCode 用户输入的验证码
     */
    @PostMapping("/validate")
    @Operation(summary = "验证验证码")
    public Map<String, Boolean> validateCaptcha(
            @RequestParam String captchaKey,
            @RequestParam String inputCode) {
        boolean valid = captchaUtil.validateCaptcha(captchaKey, inputCode);
        Map<String, Boolean> result = new HashMap<>();
        result.put("valid", valid);
        return result;
    }
}