package com.example.entity;

import lombok.Data;

/**
 * @author SongQingWei
 * @description 权限角色关系实体
 * @date 2018年05月11 13:09
 */
@Data
public class SysPermissionRole {

    private Integer id;

    private Integer roleId;

    private Integer permissionId;
}