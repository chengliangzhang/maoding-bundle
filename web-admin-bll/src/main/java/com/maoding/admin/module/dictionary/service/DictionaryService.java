package com.maoding.admin.module.dictionary.service;

import com.maoding.admin.module.dictionary.dto.DictionaryDTO;

import java.util.List;

public interface DictionaryService {

    List<DictionaryDTO> getDictionaryByCode(String code);

    List<DictionaryDTO> getCandidateType(String ruleGroup);

    List<DictionaryDTO> getCandidateByType(String ruleType);

    List<DictionaryDTO> getRuleType();
}
