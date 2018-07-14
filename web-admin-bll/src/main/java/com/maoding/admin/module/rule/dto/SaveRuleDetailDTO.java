package com.maoding.admin.module.rule.dto;

import java.util.ArrayList;
import java.util.List;

public class SaveRuleDetailDTO {

    private String id;

    private String ruleId;

    private Integer codeType;//规则值类型（1：所有人，2:具有某个权限的人，3：觉有某个角色的人，4：具体的人员）

    private List<String> ruleValueList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<String> getRuleValueList() {
        return ruleValueList;
    }

    public void setRuleValueList(List<String> ruleValueList) {
        this.ruleValueList = ruleValueList;
    }
}
