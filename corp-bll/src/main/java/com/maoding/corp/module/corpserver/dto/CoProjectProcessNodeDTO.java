package com.maoding.corp.module.corpserver.dto;

/**
 * Created by Wuwq on 2016/10/27.
 */
public class CoProjectProcessNodeDTO {

    private String id;
    private String processId;
    private String nodeName;
    private int seq;
    /**
     * 节点在流程中所排的顺序
     */
    private int nodeSeq;
    private String companyUserId;
    private String companyUserName;
    private String accountId;
    private String accountName;
    private String completeTime;
    private int status;//2:未完成，1：完成

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getNodeSeq() {
        return nodeSeq;
    }

    public void setNodeSeq(int nodeSeq) {
        this.nodeSeq = nodeSeq;
    }

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
    }

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

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
