package com.maoding.admin.module.system.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_org_role")
public class OrgRoleDO extends BaseEntity {

    private String orgId;

    private String roleCode;

    private Integer roleType;

    private String companyId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
