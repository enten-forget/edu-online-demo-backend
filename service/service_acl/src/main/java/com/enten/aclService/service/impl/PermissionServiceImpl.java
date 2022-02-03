package com.enten.aclService.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.enten.aclService.entity.Permission;
import com.enten.aclService.entity.RolePermission;
import com.enten.aclService.entity.User;
import com.enten.aclService.helper.MenuHelper;
import com.enten.aclService.helper.PermissionHelper;
import com.enten.aclService.mapper.PermissionMapper;
import com.enten.aclService.service.PermissionService;
import com.enten.aclService.service.RolePermissionService;
import com.enten.aclService.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UserService userService;

    //获取全部菜单
    @Override
    public List<Permission> queryAllMenu() {
        //1 查询菜单所有数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        //2 把查询的所有菜单list集合按照要求进行封装
        List<Permission> result = build(permissionList);

        return result;
    }

    //递归删除菜单
    @Override
    public void removeChildById(String id) {
        //1 创建list集合,用于封装所有删除菜单id值
        List<String> idList = new ArrayList<>();
        idList.add(id);
        //2 向idList集合设置删除菜单id
        this.selectChildListById(id, idList);
        // 根据id数组删除
        baseMapper.deleteBatchIds(idList);
    }

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(String roleId) {

        // 获取所有权限
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));
        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        //转换给角色id与角色权限对应Map对象
//        List<String> permissionIdList = rolePermissionList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
//        allPermissionList.forEach(permission -> {
//            if(permissionIdList.contains(permission.getId())) {
//                permission.setSelect(true);
//            } else {
//                permission.setSelect(false);
//            }
//        });
        for (Permission permission : allPermissionList) {
            for (RolePermission rolePermission : rolePermissionList) {
                if (rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }
        List<Permission> permissionList = build(allPermissionList);
        return permissionList;
    }

    //给角色分配权限
    @Override
    public void saveRolePermissionRelationShip(String roleId, String[] permissionIds) {
        // roleId角色id permissionId菜单id
        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        // 创建list集合,用于封装添加数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        // 遍历所有菜单数据
        for(String permissionId : permissionIds) {
            if(StringUtils.isEmpty(permissionId)) continue;
            // RolePermission对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            // 封装到list集合
            rolePermissionList.add(rolePermission);
        }
        // 添加到角色菜单关系表
        rolePermissionService.saveBatch(rolePermissionList);
    }

    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MenuHelper.build(permissionList);
        return result;
    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    /**
     *	递归获取子节点
     */
    private void selectChildListById(String id, List<String> idList) {
        // 根据当前菜单id,查询菜单里面子菜单id,封装到List集合中

        // 查询菜单子菜单id
        List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
        // 把childList里面菜单id值获取出来,封装idList里面,做递归查询
        childList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getId());
            //递归查询
            this.selectChildListById(item.getId(), idList);
        });
    }

    /**
     * 使用递归方法建菜单
     */
    private static List<Permission> build(List<Permission> treeNodes) {
        // 创建list集合,用于数据最终封装
        List<Permission> trees = new ArrayList<>();
        // 把所有菜单list集合遍历
        for (Permission treeNode : treeNodes) {
            // 得到顶层菜单 pid=0
            if ("0".equals(treeNode.getPid())) {
                // 设置顶层菜单的level是1
                treeNode.setLevel(1);
                // 根据顶层菜单,想里面查询子菜单,封装到trees里面
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     */
    private static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        //1 因为父节点菜单里面要放子节点菜单 所以先放一个空子节点菜单进去
        treeNode.setChildren(new ArrayList<>());
        //2 遍历所有菜单list集合,进行判断比较,比较id和pid的值是否相同
        for (Permission it : treeNodes) {
            // 判断是否有子节点(id和pid的值是否相同)
            if(treeNode.getId().equals(it.getPid())) {
                // 如果相同 成为子节点 level值加一
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                // 先初始化子菜单
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                // 把查询出来的子菜单放到父菜单里面
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }
}
