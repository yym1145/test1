package com.test.test.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class GetRoleLinkedUserListDTO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "科室ID")
    private Long departmentId;

    @Schema(description = "名称")
    private String content;

}
