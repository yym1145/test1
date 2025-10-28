package com.test.test.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.test.test.context.BaseContext;
import com.test.test.mongoDB.Message;
import com.test.test.result.Result;
import com.test.test.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Illya
 * @Date: 2024/9/3 下午5:32
 */
@RestController
@RequestMapping("/message/basics")
@Slf4j
@Tag(name = "消息中心相关接口")
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/getMessage")
    @Operation(summary = "获取所有消息")
    @ApiOperationSupport(author = "汪润杰")
    public Result<List<Message>> getMessage() {
        return Result.success(messageService.getMessage(BaseContext.getCurrentUserId()));
    }

    @DeleteMapping("/deleteMessage")
    @Operation(summary = "删除指定消息")
    @ApiOperationSupport(author = "汪润杰")
    public Result<Object> deleteMessage(@RequestParam @Parameter(description = "需要删除的消息ID", required = true) List<String> ids) {
        return Result.success(messageService.deleteMessage(ids));
    }

    @PutMapping("/readMessage")
    @Operation(summary = "已读指定消息")
    @ApiOperationSupport(author = "汪润杰")
    public Result<Object> readMessage(@RequestParam @Parameter(description = "已读的消息ID列表", required = true) List<String> ids) {
        return Result.success(messageService.readMessage(ids));
    }
}

