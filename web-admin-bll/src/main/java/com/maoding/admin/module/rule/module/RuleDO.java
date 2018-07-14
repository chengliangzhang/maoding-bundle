package com.maoding.admin.module.rule.module;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_rule")
public class RuleDO extends BaseEntity {

    private String orgId;

    private String name;

    private String description;

    private Integer ruleType;

    private Integer ruleGroup;

    private Integer seq;

    private Integer deleted;

    private Integer multiSelect;

    private Integer directionType;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
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

    public Integer getMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(Integer multiSelect) {
        this.multiSelect = multiSelect;
    }

    public Integer getRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(Integer ruleGroup) {
        this.ruleGroup = ruleGroup;
    }

    public Integer getDirectionType() {
        return directionType;
    }

    public void setDirectionType(Integer directionType) {
        this.directionType = directionType;
    }
}
