package com.test.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.test.dto.role.AddRoleDTO;
import com.test.test.vo.role.RoleGroupListVO;
import com.test.test.vo.role.RoleMenuPermissionVO;
import com.test.test.vo.role.permission.MenuPermissionVO;

import java.util.List;

public interface RoleService {
    RoleMenuPermissionVO getRoleMenuPermission(Long id);

    List<RoleGroupListVO>getRoleGroupList();

    List<MenuPermissionVO>getMenuPermission();

    String addRole(AddRoleDTO addRoleDTO) throws JsonProcessingException;
}
