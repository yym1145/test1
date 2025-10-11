package com.test.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.test.entiy.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
