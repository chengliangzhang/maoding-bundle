package com.maoding.admin.module.orgAuth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by Wuwq on 2017/05/31.
 */
@Table(name = "maoding_web_org_auth")
public class OrgAuthDO extends BaseEntity {
    /**
     * 企业名称
     */
    private String orgName;

    /**
     * 认证状态(0.默认状态，1.提交审核，2认证通过，3，认证不通过)
     */
    private Integer authenticationStatus;

    /**
     * 经办人
     */
    private String operatorName;

    /**
     * 认证不通过原因
     */
    private String rejectReason;

    /**
     * 法人代表
     */
    private String legalRepresentative;

    /**
     * 营业执照的类型（0：普通营业执照（仍然标识为15位的“注册号”），1：多证合一营业执照（原“注册号”字样，调整为18位的“统一社会信用代码”））
     */
    private Integer businessLicenseType;

    /**
     * 工商营业执照号码（注册号）
     */
    private String businessLicenseNumber;

    /**
     * 申请日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime applyDate;

    /**
     * 有效期（结束日期）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime expiryDate;

    /**
     * 删除标识
     */
    private Integer deleted;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(Integer authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public Integer getBusinessLicenseType() {
        return businessLicenseType;
    }

    public void setBusinessLicenseType(Integer businessLicenseType) {
        this.businessLicenseType = businessLicenseType;
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
