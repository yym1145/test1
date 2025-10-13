package com.test.test.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: 汪润杰
 * @Date: 24 4月 2025 15:39
 */
@Data
public class GetRoleLinkedUserListDTO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "科室ID")
    private Long departmentId;

    @Schema(description = "名称")
    private String content;

}
