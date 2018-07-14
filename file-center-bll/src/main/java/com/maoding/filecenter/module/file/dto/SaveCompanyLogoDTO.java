package com.maoding.filecenter.module.file.dto;

import com.maoding.core.bean.FastdfsUploadResult;

/**
 * Created by Wuwq on 2017/07/02.
 */
public class SaveCompanyLogoDTO extends FastdfsUploadResult {
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    private String accountId;
    private String companyId;
}
