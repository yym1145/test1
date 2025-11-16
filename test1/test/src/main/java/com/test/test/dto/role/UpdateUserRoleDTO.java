package com.test.test.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;




@Data
public class UpdateUserRoleDTO {

    @Schema(description = "用户ID",required = true)
    @NotNull(message = "用户ID为空")
    private Long userId;

    @Schema(description = "角色ID",required = true)
    @NotNull(message = "角色ID为空")
    private Long roleId;

    @Schema(description = "操作(true:增加关联;false:删除关联)",required = true)
    @NotNull(message = "操作为空")
    private Boolean operation;

}
