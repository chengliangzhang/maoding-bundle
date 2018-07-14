package com.maoding.admin.module.rule.module;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_rule_detail")
public class RuleDetailDO extends BaseEntity {

    private String ruleId;

    private String ruleValue;

    private Integer codeType;

    private Integer deleted;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
