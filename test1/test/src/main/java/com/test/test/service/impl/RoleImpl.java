package com.test.test.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.test.dto.role.AddRoleDTO;
import com.test.test.entiy.Role;
import com.test.test.enumerate.role.RoleType;
import com.test.test.exception.BaseException;
import com.test.test.mapper.RoleMapper;
import com.test.test.redis.RedisPrefix;
import com.test.test.service.RoleService;
import com.test.test.vo.role.RoleGroupListVO;
import com.test.test.vo.role.RoleMenuPermissionVO;
import com.test.test.vo.role.permission.MenuPermissionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleImpl implements RoleService {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    private final RoleMapper roleMapper;
    @Override
    public RoleMenuPermissionVO getRoleMenuPermission(Long id) {
        Role role = roleMapper.getById(id);
        if (role == null){
            throw new BaseException("角色不存在");
        }
        RoleMenuPermissionVO vo = new RoleMenuPermissionVO();
        vo.setMenuIds(roleMapper.getMenuIdsByRoleId(id).stream().map(String::valueOf).collect(Collectors.toList()));
        vo.setPermissionIds(roleMapper.getPermissionIdsByRoleId(id).stream().map(String::valueOf).collect(Collectors.toList()));
        return vo;
    }

    @Override
    public List<RoleGroupListVO> getRoleGroupList() {
        List<RoleGroupListVO> voList = roleMapper.getRoleGroupList();
        for (RoleGroupListVO vo : voList) {
            vo.setGroupName(vo.getRoleType().getDesc());
        }
        return voList;
    }

    @Override
    public List<MenuPermissionVO> getMenuPermission() {
        List<MenuPermissionVO> mpList = roleMapper.getMenuPermission();
        //创建Map存储ID-菜单权限映射防止多次遍历
        Map<Long, MenuPermissionVO> menuMap = new HashMap<Long, MenuPermissionVO>();
        //创建根菜单列表
        List<MenuPermissionVO> rootMenus = new ArrayList<>();
        //第一次遍历
        for (MenuPermissionVO menu : mpList) {
            //判断父菜单是否为空,为空则为根菜单
            if (menu.getParentId() == null) {
                //判断子菜单列表是否为空
                //存入Map和根菜单List中
                rootMenus.add(menu);
            }
            menuMap.put(menu.getId(), menu);
        }
        //第二次遍历菜单列表以构建父子关系
        for (MenuPermissionVO menu : menuMap.values()) {
            //判断父菜单ID是否为空
            if (menu.getParentId() == null) {
                //如果不为空则该菜单为父菜单,直接跳过
                continue;
            }
            //否则从Map中根据ID直接查询父菜单并且获得父菜单的子菜单列表将自己插入子菜单列表中
            MenuPermissionVO parentMenu = menuMap.get(menu.getParentId());
            if (parentMenu != null) {
                if (parentMenu.getChildren() == null) {
                    parentMenu.setChildren(new ArrayList<>());
                }
                parentMenu.getChildren().add(menu);
            }

        }
        //返回结果
        return rootMenus;
    }

    @Override
    public String addRole(AddRoleDTO addRoleDTO) throws JsonProcessingException {
        //创建角色,类型为自定义
        Role role = new Role();
        role.setId(IdWorker.getId());
        role.setName(addRoleDTO.getName());
        role.setDesc(addRoleDTO.getDesc());
        role.setType(RoleType.CUSTOM);
        roleMapper.insert(role);
        //增加角色-用户关联
        roleMapper.addUserRole(role.getId(), addRoleDTO.getUserIds());
        //增加角色-菜单关联
        redisTemplate.opsForValue().set(RedisPrefix.ROLE_DATA.getPrefix() + role.getId(), objectMapper.writeValueAsString(role));
        return role.getId().toString() + "";
    }
}
