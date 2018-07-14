package com.maoding.admin.module.rule.dto;

import com.maoding.admin.module.dictionary.dto.DictionaryDTO;

import java.util.ArrayList;
import java.util.List;

public class RuleValueDTO extends DictionaryDTO {

    private Integer isSelect;

    List<RuleDetailDTO> valueList = new ArrayList<>();

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

    public List<RuleDetailDTO> getValueList() {
        return valueList;
    }

    public void setValueList(List<RuleDetailDTO> valueList) {
        this.valueList = valueList;
    }
}
