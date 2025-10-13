package com.test.test.vo.role.menu;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author: 汪润杰
 * @Date: 22 4月 2025 15:29
 */
@Data
public class MenuVO {
    @Schema(description = "菜单ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单路径")
    private String path;

    @Schema(description = "父菜单ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    @Schema(description = "子菜单")
    private List<MenuVO> children;
}
