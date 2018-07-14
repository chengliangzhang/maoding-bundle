package com.maoding.yongyoucloud.module.enterprise.dto;

public class EnterpriseDTO {
    private String id;

    private String corpname;

    private String companyId;

    private String enterpriseOrgId;

    private String linkmanNames;//多联系人用逗号隔开链接的字符串

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEnterpriseOrgId() {
        return enterpriseOrgId;
    }

    public void setEnterpriseOrgId(String enterpriseOrgId) {
        this.enterpriseOrgId = enterpriseOrgId;
    }

    public String getLinkmanNames() {
        return linkmanNames;
    }

    public void setLinkmanNames(String linkmanNames) {
        this.linkmanNames = linkmanNames;
    }
}
