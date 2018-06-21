package com.example.entity;

import lombok.Data;

import java.util.List;

/**
 * @author SongQingWei
 * @description 角色实体
 * @date 2018年05月11 13:09
 */
@Data
public class SysRole {

    private Integer id;

    private String roleName;

    private Integer roleCode;

    private List<SysPermission> permissions;
}