package com.test.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.test.bo.UserLoginVerifyData;
import com.test.test.entiy.User;
import com.test.test.vo.user.CurrentUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    UserLoginVerifyData getUserLoginDataByAccount(String account);

    @Select("select * from user where id=#{id}")
    CurrentUserVO getCurrentUserInformation(Long id);

    void updateUser(User user);
}
