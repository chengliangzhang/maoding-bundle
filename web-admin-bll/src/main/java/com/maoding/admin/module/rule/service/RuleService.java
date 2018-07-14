package com.maoding.admin.module.rule.service;

import com.maoding.admin.module.dictionary.dto.DictionaryDTO;
import com.maoding.admin.module.rule.dto.RuleValueDTO;
import com.maoding.admin.module.rule.dto.SaveRuleDTO;
import com.maoding.admin.module.rule.dto.SaveRuleDetailDTO;
import com.maoding.core.bean.ApiResult;

import java.util.List;
import java.util.Map;

public interface RuleService {

    ApiResult saveRule(SaveRuleDTO dto) throws Exception;

    ApiResult saveRuleDetail(SaveRuleDetailDTO dto) throws Exception;

    ApiResult getRuleDetail(String id) throws Exception;

    Object getCandidateValue(Integer codeType) throws Exception;

    List<RuleValueDTO> getCandidateByType(String ruleType) throws Exception;
}
