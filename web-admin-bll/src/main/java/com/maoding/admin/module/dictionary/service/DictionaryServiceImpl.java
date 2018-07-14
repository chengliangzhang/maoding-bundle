package com.maoding.admin.module.dictionary.service;

import com.maoding.admin.module.dictionary.dao.DictionaryDao;
import com.maoding.admin.module.dictionary.dto.DictionaryDTO;
import com.maoding.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseService implements DictionaryService {

    @Autowired
    private DictionaryDao dictionaryDao;

    @Override
    public List<DictionaryDTO> getDictionaryByCode(String code) {
        return dictionaryDao.getDictionaryByCode(code);
    }

    @Override
    public List<DictionaryDTO> getCandidateType(String ruleGroup) {
        switch (ruleGroup){
            case "1":
                return this.getDictionaryByCode("person_candidate_type");
        }
        return null;
    }

    @Override
    public List<DictionaryDTO> getCandidateByType(String ruleType) {
        if("1".equals(ruleType) || "2".equals(ruleType) || "5".equals(ruleType) || "6".equals(ruleType)){
            return this.getDictionaryByCode("person_candidate_type");
        }
        return null;
    }

    @Override
    public List<DictionaryDTO> getRuleType() {
        return this.getDictionaryByCode("rule_type");
    }
}
