package com.enten.aclService.controller;

import com.enten.aclService.entity.Permission;
import com.enten.aclService.service.PermissionService;
import com.enten.commonutils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限 菜单管理
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/permission")
//@CrossOrigin
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    //获取全部菜单
    @ApiOperation(value = "查询所有菜单")
    @GetMapping
    public Result indexAllPermission() {
        List<Permission> list =  permissionService.queryAllMenu();
        return Result.ok().data("children",list);
    }

    //删除该类菜单
    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        permissionService.removeChildById(id);
        return Result.ok();
    }

    //给角色分配权限
    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(String roleId,String[] permissionId) {
        permissionService.saveRolePermissionRelationShip(roleId,permissionId);
        return Result.ok();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable String roleId) {
        List<Permission> list = permissionService.selectAllMenu(roleId);
        return Result.ok().data("children", list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.ok();
    }

}

