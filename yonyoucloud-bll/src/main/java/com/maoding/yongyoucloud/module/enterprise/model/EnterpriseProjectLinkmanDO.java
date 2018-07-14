package com.maoding.yongyoucloud.module.enterprise.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_web_enterprise_project_linkman")
public class EnterpriseProjectLinkmanDO extends BaseEntity{

    private String enterpriseOrgId;

    private String linkmanId;

    private String projectId;

    private String position;

    public String getEnterpriseOrgId() {
        return enterpriseOrgId;
    }

    public void setEnterpriseOrgId(String enterpriseOrgId) {
        this.enterpriseOrgId = enterpriseOrgId == null ? null : enterpriseOrgId.trim();
    }

    public String getLinkmanId() {
        return linkmanId;
    }

    public void setLinkmanId(String linkmanId) {
        this.linkmanId = linkmanId == null ? null : linkmanId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }
}