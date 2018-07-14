package com.maoding.admin.module.rule;

import com.maoding.admin.module.dictionary.service.DictionaryService;
import com.maoding.admin.module.rule.dto.SaveRuleDTO;
import com.maoding.admin.module.rule.dto.SaveRuleDetailDTO;
import com.maoding.admin.module.rule.service.RuleService;
import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rule")
public class RuleController extends BaseController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping("/saveRule")
    @ResponseBody
    public ApiResult saveRule(@RequestBody SaveRuleDTO dto) throws Exception{
        return ruleService.saveRule(dto);
    }

    @RequestMapping("/saveRuleDetail")
    @ResponseBody
    public ApiResult saveRuleDetail(@RequestBody SaveRuleDetailDTO dto) throws Exception{
        return ruleService.saveRuleDetail(dto);
    }

    @RequestMapping(value = "/getRuleType",method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getRuleType() throws Exception{
        return ApiResult.success(dictionaryService.getRuleType());
    }

    @RequestMapping(value = "/getCandidateType/{ruleType}",method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getCandidateType(@PathVariable("ruleType") String ruleType) throws Exception{
        return ApiResult.success(ruleService.getCandidateByType(ruleType));
    }

    @RequestMapping(value = "/getCandidateValue/{codeType}",method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getCandidateValue(@PathVariable("codeType") Integer codeType) throws Exception{
        return ApiResult.success(ruleService.getCandidateValue(codeType));
    }

    @RequestMapping("/getRuleDetail")
    @ResponseBody
    public ApiResult getRuleDetail(@RequestBody SaveRuleDTO dto) throws Exception{
        return ruleService.saveRule(dto);
    }

}
