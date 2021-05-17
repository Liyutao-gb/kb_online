package com.atguigu.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.entity.RolePermission;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.helper.MemuHelper;
import com.atguigu.aclservice.helper.PermissionHelper;
import com.atguigu.aclservice.mapper.PermissionMapper;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.RolePermissionService;
import com.atguigu.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author lytstart
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserService userService;


    //========================递归查询所有菜单================================================

    /**
     * 获取全部菜单
     *
     * @return
     */
    @Override
    public List<Permission> queryAllMenu() {
        // 1.查询菜单表所有数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        // 2.把查询所有list菜单集合，进行封装
        List<Permission> resultList = bulidPermission(permissionList);

        return resultList;
    }

    /**
     * 把返回所有菜单list集合进行封装的方法
     *
     * @param permissionList
     * @return
     */
    public static List<Permission> bulidPermission(List<Permission> permissionList) {

        // finalNode用于数据的最终封装
        List<Permission> finalNode = new ArrayList<>();

        // 把所有菜单list集合遍历，得到顶层菜单pid=0菜单，设置level=1
        for (Permission permission : permissionList) {
            // 得到顶层菜单pid=0菜单
            if ("0".equals(permission.getPid())) {
                // 设置顶层菜单level=1
                permission.setLevel(1);
                finalNode.add(selectChildren(permission, permissionList));
            }
        }
        return finalNode;
    }

    /**
     * 封装permisson菜单，最终以树形的格式(json)返回
     * @param permissionNode 全部的菜单权限
     * @param permissionList 最终返回的List集合，树形结构
     * @return
     */
    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {

        // 1.向一层菜单里面放二层菜单，二层里面还要放三层，把对象初始化
        permissionNode.setChildren(new ArrayList<Permission>());

        // 2.遍历所有菜单list集合，进行判断比较，比较id和pid值是否相同
        for (Permission it : permissionList) {
            //id和pid：一级菜单和二级菜单
            if (permissionNode.getId().equals(it.getPid())) {
                // 把父菜单的level值+1，level=2
                int level = permissionNode.getLevel() + 1;
                it.setLevel(level);
                //完善一下代码
                if (permissionNode.getChildren() == null) {
                    permissionNode.setChildren(new ArrayList<Permission>());
                }
                permissionNode.getChildren().add(selectChildren(it, permissionList));
            }
        }

        // 如果是最高级level返回Permission对象
        return permissionNode;
    }

    /**
     * ============递归删除菜单==================================
     *
     * @param id
     */
    @Override
    public void removeChildById(String id) {

        // 1.创建List集合，用于封装所有删除菜单id值
        List<String> idList = new ArrayList<>();
        // 2.向idList集合设置删除菜单id
        this.selectPermissionChildById(id, idList);
        // 封装当前id
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    /**
     * 2 根据当前菜单id查询 子菜单id，封装到list集合
     *
     * @param id
     * @param idList
     */
    private void selectPermissionChildById(String id, List<String> idList) {

        // 查询菜单里面子菜单id
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", id);
        //查询id单独列
        wrapper.select("id");
        // 查询子菜单id值
        List<Permission> childIdList = baseMapper.selectList(wrapper);

        // 把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        // 和for循环一样
        childIdList.stream().forEach(item -> {
            // 封装idList里面
            idList.add(item.getId());
            //递归查询
            this.selectPermissionChildById(item.getId(), idList);
        });

    }


    /**
     * =========================给角色分配菜单=================================
     *
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void saveRolePermissionRealtionShip(String roleId, String[] permissionIds) {

        // roleId:角色id
        // permissionIds[]菜单id 数组

        // 1.创建List集合，封装添加数据
        List<RolePermission> rolePermissions = new ArrayList<>();
        // 遍历数组
        for (String pid : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(pid);
            // 封装list集合
            rolePermissions.add(rolePermission);
        }

        // 添加到关系表RolePermission中
        rolePermissionService.saveBatch(rolePermissions);
    }


    /**
     * 根据角色获取菜单
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> selectAllMenu(String roleId) {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        //转换给角色id与角色权限对应Map对象
//        List<String> permissionIdList = rolePermissionList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
//        allPermissionList.forEach(permission -> {
//            if(permissionIdList.contains(permission.getId())) {
//                permission.setSelect(true);
//            } else {
//                permission.setSelect(false);
//            }
//        });
        for (int i = 0; i < allPermissionList.size(); i++) {
            Permission permission = allPermissionList.get(i);
            for (int m = 0; m < rolePermissionList.size(); m++) {
                RolePermission rolePermission = rolePermissionList.get(m);
                if (rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }


        List<Permission> permissionList = bulidPermission(allPermissionList);
        return permissionList;
    }

    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if (this.isSysAdmin(id)) {
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
        if (this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }

    /**
     * 判断用户是否系统管理员
     *
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if (null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }


}
