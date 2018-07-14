package com.maoding.filecenter.module.file.dto;

/**
 * Created by Wuwq on 2017/05/27.
 */
public class DeleteDTO {
    private String accountId;
    private String pid;
    private String id;
    private String fileName;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
