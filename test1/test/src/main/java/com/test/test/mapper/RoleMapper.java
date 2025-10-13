package com.test.test.mapper;

import com.test.test.entiy.Role;
import com.test.test.vo.role.RoleGroupListVO;
import com.test.test.vo.role.permission.MenuPermissionVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM role WHERE id = #{id}")
    Role getById(Long id);

    @Select("SELECT menu_id FROM role_menu WHERE role_id = #{id}")
    List<Long> getMenuIdsByRoleId(Long id);

    @Select("SELECT permission_id FROM role_permission WHERE role_id = #{id}")
    List<Long> getPermissionIdsByRoleId(Long id);


    List<RoleGroupListVO> getRoleGroupList();

    List<MenuPermissionVO> getMenuPermission();

    @Insert("INSERT INTO role (id, name, `desc`, type) VALUES (#{id}, #{name}, #{desc}, #{type})")
    Integer insert(Role role);

    Integer addUserRole(Long id, List<Long> list);
}
