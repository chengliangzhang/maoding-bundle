package com.maoding.admin.module.rule.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleDTO {

    private String id;

    private String name;

    private String description;

    private String orgId;

    private Integer ruleType;

    private Integer multiSelect;

    private List<RuleValueDTO> ruleValueList = new ArrayList<>();

    private Map<String,Object> ruleParam = new HashMap<>();

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

    public List<RuleValueDTO> getRuleValueList() {
        return ruleValueList;
    }

    public void setRuleValueList(List<RuleValueDTO> ruleValueList) {
        this.ruleValueList = ruleValueList;
    }

    public Map<String, Object> getRuleParam() {
        return ruleParam;
    }

    public void setRuleParam(Map<String, Object> ruleParam) {
        this.ruleParam = ruleParam;
    }
}
