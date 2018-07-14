package com.maoding.admin.module.orgAuth.dto;

/**
 * Created by Wuwq on 2017/05/31.
 */
public class OrgAuthAuditDTO {
    /**
     * 组织ID
     */
    String id;
    /**
     * 认证状态(2:通过 3：不通过)
     */
    Integer authenticationStatus;
    /**
     * 认证审核人姓名
     */
    String auditorName;
    /**
     * 认证无法通过原因分类
     */
    Integer rejectType;
    /**
     * 认证不通过原因文字解释
     */
    String rejectReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(Integer authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public Integer getRejectType() {
        return rejectType;
    }

    public void setRejectType(Integer rejectType) {
        this.rejectType = rejectType;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
