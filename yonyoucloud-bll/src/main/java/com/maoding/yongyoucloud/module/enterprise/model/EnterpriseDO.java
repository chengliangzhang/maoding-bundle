package com.maoding.yongyoucloud.module.enterprise.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Table(name = "maoding_web_enterprise")
public class EnterpriseDO extends BaseEntity{

    private String corpname;

    private String address;

    private String creditcode;

    private String gongsh;

    private String orgcode;

    private String state;

    private String type;

    private Date regtime;

    private String proxyer;

    private String money;

    private String regpart;

    private String shortcut;

    private String industry;

    private String taxNumber;

    private String hasUnify;

    private String fieldrange;

    //1=工商数据,2=手工输入
    private Integer enterpriseType;

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname == null ? null : corpname.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCreditcode() {
        return creditcode;
    }

    public void setCreditcode(String creditcode) {
        this.creditcode = creditcode == null ? null : creditcode.trim();
    }

    public String getGongsh() {
        return gongsh;
    }

    public void setGongsh(String gongsh) {
        this.gongsh = gongsh == null ? null : gongsh.trim();
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode == null ? null : orgcode.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getRegtime() {
        return regtime;
    }

    public void setRegtime(Date regtime) {
        this.regtime = regtime;
    }

    public String getProxyer() {
        return proxyer;
    }

    public void setProxyer(String proxyer) {
        this.proxyer = proxyer == null ? null : proxyer.trim();
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money == null ? null : money.trim();
    }

    public String getRegpart() {
        return regpart;
    }

    public void setRegpart(String regpart) {
        this.regpart = regpart == null ? null : regpart.trim();
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut == null ? null : shortcut.trim();
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber == null ? null : taxNumber.trim();
    }

    public String getHasUnify() {
        return hasUnify;
    }

    public void setHasUnify(String hasUnify) {
        this.hasUnify = hasUnify == null ? null : hasUnify.trim();
    }

    public String getFieldrange() {
        return fieldrange;
    }

    public void setFieldrange(String fieldrange) {
        this.fieldrange = fieldrange == null ? null : fieldrange.trim();
    }

    public Integer getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(Integer enterpriseType) {
        this.enterpriseType = enterpriseType;
    }
}