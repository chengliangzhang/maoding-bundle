package com.maoding.im.module.imAccount.dto;

public class ImAccountSyncDTO {

    private String accountId;

    private String accountName;

    private Integer accountStatus;

    private String imAccountId;//此处为im的账号 = accountId,为了同步im，用于查询是否为null如果为null，则没有创建im

    private String upVersion;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getImAccountId() {
        return imAccountId;
    }

    public void setImAccountId(String imAccountId) {
        this.imAccountId = imAccountId;
    }

    public String getUpVersion() {
        return upVersion;
    }

    public void setUpVersion(String upVersion) {
        this.upVersion = upVersion;
    }
}
