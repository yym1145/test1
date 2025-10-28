package com.test.test.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class AddGrowthRecordCommentDTO {
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @Schema(description = "成长记录ID", defaultValue = "1", required = true)
    private Long growthRecordId;
    @Schema(description = "成长记录评论", required = true)
    private String comment;
}
