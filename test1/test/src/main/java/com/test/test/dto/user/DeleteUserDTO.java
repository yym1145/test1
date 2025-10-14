package com.test.test.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DeleteUserDTO {
    @Schema(description = "要删除的用户id")
    @NotEmpty(message = "数组不能为空")
    private List<String> idlist;
}
