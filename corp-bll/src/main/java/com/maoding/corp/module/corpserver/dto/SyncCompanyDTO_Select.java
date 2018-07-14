package com.maoding.corp.module.corpserver.dto;

/**
 * Created by Wuwq on 2017/2/9.
 * 组织同步清单
 */
public class SyncCompanyDTO_Select {

    private String id;
    /**
     * 协同端id
     */
    private String corpEndpoint;
    private String companyId;
    private String companyName;
    /**
     * 总容量
     */
    private Long totalSize;
    /**
     * 协同文档是否云端存储
     */
    private Boolean corpOnCloud;
    /**
     * 协同占用容量
     */
    private Long corpSize;
    /**
     * 文档库占用容量
     */
    private Long docmgrSize;
    /**
     * 其他占用容量
     */
    private Long otherSize;
    /**
     * 剩余容量
     */
    private Long freeSize;
    /**
     * 备注
     */
    private String remarks;

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Boolean getCorpOnCloud() {
        return corpOnCloud;
    }

    public void setCorpOnCloud(Boolean corpOnCloud) {
        this.corpOnCloud = corpOnCloud;
    }

    public Long getCorpSize() {
        return corpSize;
    }

    public void setCorpSize(Long corpSize) {
        this.corpSize = corpSize;
    }

    public Long getDocmgrSize() {
        return docmgrSize;
    }

    public void setDocmgrSize(Long docmgrSize) {
        this.docmgrSize = docmgrSize;
    }

    public Long getOtherSize() {
        return otherSize;
    }

    public void setOtherSize(Long otherSize) {
        this.otherSize = otherSize;
    }

    public Long getFreeSize() {
        return freeSize;
    }

    public void setFreeSize(Long freeSize) {
        this.freeSize = freeSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
