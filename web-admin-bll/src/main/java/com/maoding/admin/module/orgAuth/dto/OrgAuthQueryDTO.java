package com.maoding.admin.module.orgAuth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.CoreQueryDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;


/**
 * Created by Chengliang.zhang on 2017/7/12.
 */
public class OrgAuthQueryDTO extends CoreQueryDTO {
    /**
     * 要显示的认证状态(0.否，1.申请认证，2.通过认证，3.未通过认证)
     */
    Integer authenticationStatus;

    /**
     * 申请审核日期需在此日期之前
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    LocalDate applyDateBefore;
    /**
     * 申请审核日期需在此日期之后
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    LocalDate applyDateAfter;
    /**
     * 有效期需在此日期之前
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    LocalDate expiryDateBefore;

    /**
     * 审核人姓名过滤条件
     */
    String auditNameMask;
    /**
     * 经办人姓名过滤条件
     */
    String operatorNameMask;
    /**
     * 组织法定名称过滤条件
     */
    String orgNameMask;
    /**
     * 组织在卯丁内定义名称的过滤条件
     */
    String orgAliasMask;

    /**
     * 排序条件
     */
    Map<String,Integer> orderCondition;

    public String getAuditNameMask() {
        return auditNameMask;
    }

    public void setAuditNameMask(String auditNameMask) {
        this.auditNameMask = auditNameMask;
    }

    public Map<String, Integer> getOrderCondition() {
        return orderCondition;
    }

    public void setOrderCondition(Map<String, Integer> orderCondition) {
        this.orderCondition = orderCondition;
    }

    public Integer getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(Integer authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public LocalDate getApplyDateBefore() {
        return applyDateBefore;
    }

    public void setApplyDateBefore(LocalDate applyDateBefore) {
        this.applyDateBefore = applyDateBefore;
    }

    public LocalDate getApplyDateAfter() {
        return applyDateAfter;
    }

    public void setApplyDateAfter(LocalDate applyDateAfter) {
        this.applyDateAfter = applyDateAfter;
    }

    public LocalDate getExpiryDateBefore() {
        return expiryDateBefore;
    }

    public void setExpiryDateBefore(LocalDate expiryDateBefore) {
        this.expiryDateBefore = expiryDateBefore;
    }

    public String getOrgNameMask() {
        return orgNameMask;
    }

    public void setOrgNameMask(String orgNameMask) {
        this.orgNameMask = orgNameMask;
    }

    public String getOrgAliasMask() {
        return orgAliasMask;
    }

    public void setOrgAliasMask(String orgAliasMask) {
        this.orgAliasMask = orgAliasMask;
    }

    public String getOperatorNameMask() {
        return operatorNameMask;
    }

    public void setOperatorNameMask(String operatorNameMask) {
        this.operatorNameMask = operatorNameMask;
    }
}
