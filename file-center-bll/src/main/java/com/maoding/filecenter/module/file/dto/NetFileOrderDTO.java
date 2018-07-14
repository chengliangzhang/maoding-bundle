package com.maoding.filecenter.module.file.dto;

import java.util.List;

/**
 * Created by Wuwq on 2017/06/06.
 */
public class NetFileOrderDTO {
    private String companyId;
    private String accountId;
    private List<String> fileIds;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }
}
