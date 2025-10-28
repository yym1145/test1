package com.test.test.entiy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GrowthRecordComment {
    private Long id;

    @NotNull(message = "成长记录ID不能为空")
    private Long growthRecordId;

    @NotBlank(message = "评论内容不能为空")
    private String comment;

    @NotNull(message = "创建用户不能为空")
    private Long createUser;
}
