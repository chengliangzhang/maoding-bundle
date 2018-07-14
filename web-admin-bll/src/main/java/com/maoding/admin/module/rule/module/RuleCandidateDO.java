package com.maoding.admin.module.rule.module;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_rule_candidate")
public class RuleCandidateDO extends BaseEntity {

    private String ruleId;

    private Integer codeType;

    private Integer seq;

    private Integer deleted;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
