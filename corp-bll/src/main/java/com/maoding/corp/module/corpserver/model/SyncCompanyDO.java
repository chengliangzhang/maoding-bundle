package com.maoding.corp.module.corpserver.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

/**
 * Created by Wuwq on 2017/2/9.
 * 组织同步清单
 */
@Table(name = "maoding_corp_sync_company")
public class SyncCompanyDO extends BaseEntity {

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
