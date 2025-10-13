package com.test.test.entiy;

import com.test.test.enumerate.role.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Role {
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色描述")
    private String desc;

    @Schema(description = "角色类型")
    private RoleType type;
}
