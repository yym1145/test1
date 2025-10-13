package com.test.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.test.test.dto.role.AddRoleDTO;
import com.test.test.result.Result;
import com.test.test.service.RoleService;
import com.test.test.vo.role.RoleGroupListVO;
import com.test.test.vo.role.RoleMenuPermissionVO;
import com.test.test.vo.role.permission.MenuPermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "角色管理")
public class RoleController {
    private final RoleService roleService;
    @GetMapping("/getRoleMenuPermission/{id}")
    @Operation(summary = "获取角色菜单权限")
    public Result<RoleMenuPermissionVO> getRoleMenuPermission(@PathVariable Long id) {
        return Result.success("查询成功", roleService.getRoleMenuPermission(id));
    }


    @GetMapping("/getRoleGroupList")
    @Operation(summary = "获取角色列表")
    public Result<List<RoleGroupListVO>> getRoleGroupList() {
        return Result.success("获取角色列表成功", roleService.getRoleGroupList());
    }

    @GetMapping("/permission/getMenuPermission")
    @Operation(summary = "获取菜单权限")
    public Result<List<MenuPermissionVO>> getMenuPermission() {
        return Result.success("查询成功", roleService.getMenuPermission());
    }

    @PostMapping("/add")
    @Operation(summary = "新增角色")
    public Result<String> addRole(@Valid @RequestBody AddRoleDTO dto) throws JsonProcessingException {
        return Result.success("新增角色成功",roleService.addRole(dto));
    }


}
