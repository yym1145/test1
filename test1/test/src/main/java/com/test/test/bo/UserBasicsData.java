package com.test.test.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: 汪润杰
 * @Date: 20 4月 2025 10:02
 */
@Data
public class UserBasicsData {

    @Schema(description = "用户ID")
    private String Id;

    @Schema(description = "用户名称")
    private String name;
}
