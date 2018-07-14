package com.maoding.common.module.companyDisk.dto;

/**
 * Created by Wuwq on 2017/06/09.
 */
public class RecalcSizeDTO {
    private String companyId;
    private Integer sumType;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getSumType() {
        return sumType;
    }

    public void setSumType(Integer sumType) {
        this.sumType = sumType;
    }
}
