package com.maoding.corp.module.corpserver.dto;

/**
 * Created by Wuwq on 2017/2/9.
 * 组织同步清单
 */
public class SyncCompanyDTO_Create {

    /**
     * 协同端id
     */
    private String corpEndpoint;
    /**
     * 组织Id
     */
    private String companyId;
    /**
     * 备注
     */
    private String remarks;

    public String getCorpEndpoint() {
        return corpEndpoint;
    }

    public void setCorpEndpoint(String corpEndpoint) {
        this.corpEndpoint = corpEndpoint;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
