package com.maoding.corp.module.corpserver.dto;

/**
 * Created by Wuwq on 2017/2/9.
 * 组织同步清单
 */
public class SyncCompanyDTO_Update {

    private String id;
    /**
     * 备注
     */
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
