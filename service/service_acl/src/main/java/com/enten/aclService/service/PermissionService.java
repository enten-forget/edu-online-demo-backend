package com.enten.aclService.service;

import com.alibaba.fastjson.JSONObject;
import com.enten.aclService.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface PermissionService extends IService<Permission> {

    //获取全部菜单
    List<Permission> queryAllMenu();

    //递归删除菜单
    void removeChildById(String id);

    //给角色分配权限
    void saveRolePermissionRelationShip(String roleId, String[] permissionId);

    //根据角色获取菜单
    List<Permission> selectAllMenu(String roleId);

    //根据用户id获取用户菜单
    List<String> selectPermissionValueByUserId(String id);

    List<JSONObject> selectPermissionByUserId(String id);
}
