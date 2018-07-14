package com.maoding.yongyoucloud.module.enterprise.dto;

import com.maoding.yongyoucloud.module.enterprise.model.EnterpriseOrgLinkmanDO;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseProjectLinkmanDTO {

    private String projectId;

    private String projectName;

    private String enterpriseOrgId;

    private int editFlag;

    private List<OperateLinkmanDTO> linkmanList = new ArrayList<>();

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEnterpriseOrgId() {
        return enterpriseOrgId;
    }

    public void setEnterpriseOrgId(String enterpriseOrgId) {
        this.enterpriseOrgId = enterpriseOrgId;
    }

    public List<OperateLinkmanDTO> getLinkmanList() {
        return linkmanList;
    }

    public void setLinkmanList(List<OperateLinkmanDTO> linkmanList) {
        this.linkmanList = linkmanList;
    }

    public int getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(int editFlag) {
        this.editFlag = editFlag;
    }
}
