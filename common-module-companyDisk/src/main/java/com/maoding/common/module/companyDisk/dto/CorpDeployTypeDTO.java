package com.maoding.common.module.companyDisk.dto;

/**
 * Created by Wuwq on 2017/06/09.
 */
public class CorpDeployTypeDTO {
    private String companyId;
    private Boolean corpOnCloud;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Boolean getCorpOnCloud() {
        return corpOnCloud;
    }

    public void setCorpOnCloud(Boolean corpOnCloud) {
        this.corpOnCloud = corpOnCloud;
    }
}
