package com.test.test.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

@Data
public class UpdateRoleDTO {

    @Schema(description = "角色ID",required = true)
    @NotNull(message = "角色ID为空")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色描述")
    private String desc;

    @Schema(description = "关联菜单ID列表")
    private List<Long> menuIds;

    @Schema(description = "关联权限ID列表")
    private List<Long> permissionIds;

    @Schema(description = "关联用户ID列表")
    private List<Long> userIds;

}
