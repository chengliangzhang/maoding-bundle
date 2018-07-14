package com.maoding.admin.module.rule.dto;

import java.util.ArrayList;
import java.util.List;

public class SaveRuleDTO {

    private String id;

    private String name;

    private String description;

    private String orgId;

    private Integer ruleType;

    private Integer multiSelect;

    List<SaveRuleDetailDTO> valueList = new ArrayList<>();

    private List<Integer> ruleCandidateList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(Integer multiSelect) {
        this.multiSelect = multiSelect;
    }

    public List<Integer> getRuleCandidateList() {
        return ruleCandidateList;
    }

    public void setRuleCandidateList(List<Integer> ruleCandidateList) {
        this.ruleCandidateList = ruleCandidateList;
    }

    public List<SaveRuleDetailDTO> getValueList() {
        return valueList;
    }

    public void setValueList(List<SaveRuleDetailDTO> valueList) {
        this.valueList = valueList;
    }
}
