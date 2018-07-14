package com.maoding.admin.module.system.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_role_permission")
public class RolePermissionDO extends BaseEntity {

    private String roleCode;

    private String permissionCode;

    private String companyId;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
