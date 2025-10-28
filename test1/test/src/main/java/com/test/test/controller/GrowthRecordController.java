package com.test.test.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.test.test.dto.AddGrowthRecordCommentDTO;
import com.test.test.result.Result;
import com.test.test.service.GrowthRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/growthRecord")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "成长记录管理")
public class GrowthRecordController {

    private final GrowthRecordService growthRecordService;

    /**
     * 添加成长记录评论
     *
     * @param addGrowthRecordCommentDTO
     * @return
     */
    @PostMapping("/addGrowthRecordComment")
    @Operation(summary = "公开成长记录列表—添加评论")
    public Result addGrowthRecordComment(@RequestBody AddGrowthRecordCommentDTO addGrowthRecordCommentDTO) {
        try {
            Long growthRecordCommentId = growthRecordService.addGrowthRecordComment(addGrowthRecordCommentDTO);
            return Result.success("评论成功,评论ID为", growthRecordCommentId);
        } catch (IOException e) {
            return Result.error(e.getMessage());
        }
    }
}
