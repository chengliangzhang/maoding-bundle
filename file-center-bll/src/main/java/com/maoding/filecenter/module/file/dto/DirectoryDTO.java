package com.maoding.filecenter.module.file.dto;

/**
 * Created by Wuwq on 2017/05/27.
 */
public class DirectoryDTO {
    /**
     * 组织ID
     */
    private String companyId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 父目录id
     */
    private String pid;
    /**
     * 操作人账号
     */
    private String accountId;
    /**
     * 目录名
     */
    private String fileName;

    private Integer type;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
