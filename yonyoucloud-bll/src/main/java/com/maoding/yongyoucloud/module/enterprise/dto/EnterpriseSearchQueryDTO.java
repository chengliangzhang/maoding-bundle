package com.maoding.yongyoucloud.module.enterprise.dto;

import com.maoding.core.base.BaseQueryDTO;

public class EnterpriseSearchQueryDTO extends BaseQueryDTO {

    private String enterpriseId;

    private String name;

    private int size;

    private String companyId;

    private String enterpriseOrgId;//app端查询甲方详情使用该字段

    private String projectId;

    private String accountId;

    boolean isSave;

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getName() {
        return name!=null?name.trim():null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        if(size==0){
            size=20;
        }
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public String getEnterpriseOrgId() {
        return enterpriseOrgId;
    }

    public void setEnterpriseOrgId(String enterpriseOrgId) {
        this.enterpriseOrgId = enterpriseOrgId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
