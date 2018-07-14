package com.maoding.filecenter.module.file.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuwq on 2017/05/27.
 */
public class NetFileDTO {

    private String companyId;
    private String accountId;
    private String id;

    private List<String> uploadFileList = new ArrayList<>();

    private List<String> deleteFileList = new ArrayList<>();

    private List<String> ids = new ArrayList<>();

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<String> getUploadFileList() {
        return uploadFileList;
    }

    public void setUploadFileList(List<String> uploadFileList) {
        this.uploadFileList = uploadFileList;
    }

    public List<String> getDeleteFileList() {
        return deleteFileList;
    }

    public void setDeleteFileList(List<String> deleteFileList) {
        this.deleteFileList = deleteFileList;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
