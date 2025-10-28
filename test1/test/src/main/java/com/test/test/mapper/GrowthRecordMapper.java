package com.test.test.mapper;

import com.test.test.entiy.GrowthRecordComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GrowthRecordMapper {

    @Insert("insert into growth_record_comment(id,growth_record_id,comment,create_user) values" +
            "(#{id},#{growthRecordId},#{comment},#{createUser})")
    void insert(GrowthRecordComment growthRecordComment);

    @Select("Select create_user from growth_record where id = #{id}")
    Long selectUserIdByGrowthRecordId(Long id);
}
