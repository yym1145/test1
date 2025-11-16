package com.test.test.enumerate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum AssociatedContent {

    GROWTH_RECORD(0, "成长记录"),
    PROBLEM_SET(1, "问题集"),
    PROBLEM_SET_COMMENT(2, "问题集评论"),
    WAREHOUSE(3,"仓库");

    @EnumValue
    final Integer code;
    @JsonValue
    final String name;
}
