package com.maoding.admin.module.system.service;

import com.maoding.admin.module.system.dto.*;
import com.maoding.core.bean.ApiResult;

import java.util.List;

public interface SystemService{

    ApiResult saveView(SaveViewDTO dto) throws Exception;

    ApiResult saveOperatePermission(SaveOperatePermissionDTO dto) throws Exception;

    String getMaxOperateId(String viewId);

    String getMaxViewCode(String pid);

    List<ViewOperatorDTO> getViewList();

    List<ViewOperatorDTO> getParentViewList();

    /**
     * 给角色分配视图（用于组织角色）
     */
    ApiResult saveViewRole(SaveViewRoleDTO dto) throws Exception;

    /**
     * 给角色分配权限（用于项目角色）
     */
    ApiResult savePermissionRoleBatch(SavePermissionRoleDTO dto) throws Exception;

    ApiResult savePermissionRole(SavePermissionRoleDTO dto) throws Exception;

    /**
     * 获取所有供组织选择的所有角色（组织角色）
     */
    List<RoleDTO> getRoleForOrg();

    List<RoleDTO> getDefaultRole();

    List<RoleDTO> getRoleForProject();
    List<RoleDTO> getRoleForProject2(String ruleId);

    /**
     * 获取角色的权限列表
     */
    List<ViewOperatorDTO> getAllViewByRoleCode(String roleCode);

    RoleDTO getRoleByCode(String roleCode);

}
