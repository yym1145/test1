package com.test.test.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddRoleDTO {

    @Schema(description = "角色名称",required = true)
    @NotBlank(message = "角色名称为空")
    private String name;

    @Schema(description = "角色描述")
    private String desc;

    @Schema(description = "用户ID列表",required = true)
    @NotEmpty(message = "用户ID列表为空")
    private List<Long> userIds;

}
