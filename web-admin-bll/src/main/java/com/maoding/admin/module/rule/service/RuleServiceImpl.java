package com.maoding.admin.module.rule.service;

import com.maoding.admin.module.dictionary.dto.DictionaryDTO;
import com.maoding.admin.module.dictionary.service.DictionaryService;
import com.maoding.admin.module.rule.dao.RuleCandidateDAO;
import com.maoding.admin.module.rule.dao.RuleDAO;
import com.maoding.admin.module.rule.dao.RuleDetailDAO;
import com.maoding.admin.module.rule.dto.*;
import com.maoding.admin.module.rule.module.RuleCandidateDO;
import com.maoding.admin.module.rule.module.RuleDO;
import com.maoding.admin.module.rule.module.RuleDetailDO;
import com.maoding.admin.module.system.dto.RoleDTO;
import com.maoding.admin.module.system.service.SystemService;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.utils.BeanUtils;
import com.maoding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service("ruleService")
public class RuleServiceImpl extends BaseService implements RuleService {

    @Autowired
    private RuleDAO ruleDAO;

    @Autowired
    private RuleDetailDAO ruleDetailDAO;

    @Autowired
    private RuleCandidateDAO ruleCandidateDAO;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SystemService systemService;


    @Override
    public ApiResult saveRule(SaveRuleDTO dto) throws Exception {
        RuleDO rule = new RuleDO();
        BeanUtils.copyProperties(dto,rule);
        if(StringUtils.isNullOrEmpty(dto.getId())){
            rule.initEntity();
            rule.setDeleted(0);
            if(dto.getMultiSelect()==null){
                rule.setMultiSelect(0);//默认为单选
            }
            //设置规则分类
            rule.setRuleGroup(this.getRuleGroup(dto.getRuleType()));
            ruleDAO.insert(rule);
        }else {
            if(dto.getRuleType()!=null){
                rule.setRuleGroup(this.getRuleGroup(dto.getRuleType()));
            }
            ruleDAO.updateByPrimaryKeySelective(rule);
        }
        for(SaveRuleDetailDTO value:dto.getValueList()){
            value.setRuleId(rule.getId());
            saveRuleDetail(value);
        }
        saveRuleCandidate(rule.getId(),dto);
        return ApiResult.success("操作成功",null);
    }

    @Override
    public ApiResult saveRuleDetail(SaveRuleDetailDTO dto) throws Exception {
        RuleDO  rule = this.ruleDAO.selectByPrimaryKey(dto.getRuleId());
        if(rule==null){
            return ApiResult.failed("查询失败");
        }
        Example example = new Example(RuleCandidateDO.class);
        example.createCriteria()
                .andCondition("rule_id = ", dto.getRuleId())
                .andCondition("code_type = ", dto.getCodeType());
        ruleDetailDAO.deleteByExample(example);
        RuleDetailDO detail = new RuleDetailDO();
        BeanUtils.copyProperties(dto,detail);
        if(rule.getRuleGroup()==1){
            for(String value:dto.getRuleValueList()){
                detail.initEntity();
                detail.setRuleValue(value);
                ruleDetailDAO.insert(detail);
            }
        }else {
            return ApiResult.error("系统暂不支持",null);
        }
        return ApiResult.success("操作成功",null);
    }

    @Override
    public ApiResult getRuleDetail(String id) throws Exception{
        RuleDO  rule = this.ruleDAO.selectByPrimaryKey(id);
        if(rule==null){
            return ApiResult.failed("查询失败");
        }
        RuleDTO ruleDTO = new RuleDTO();
        if(rule.getRuleGroup()==1){
            List<DictionaryDTO> list = dictionaryService.getCandidateType(rule.getRuleGroup()+"");
            List<RuleValueDTO> valueList = BeanUtils.copyFields(list,RuleValueDTO.class);
            //已设置的值
            getRuleValue(id,valueList);
            ruleDTO.setRuleValueList(valueList);
        }
        return ApiResult.success(ruleDTO);
    }

    //（1：企业所有人员，2:部门人员，3：具有角色成员，4：具有权限成员,5:个人,6:群组）',
    @Override
    public Object getCandidateValue(Integer codeType) throws Exception {

        if(codeType==1){
            //所有人
        }
        if(codeType==2){
            //查询部门
        }
        if(codeType==3){
            //查询所有默认的权限
          return  systemService.getRoleForProject();
        }
        if(codeType==4){
            //查询所有默认的权限
        }
        if(codeType==5){

        }
        if(codeType==6){

        }

        return null;

    }

    @Override
    public List<RuleValueDTO> getCandidateByType(String ruleType) throws Exception{
        List<DictionaryDTO> list = dictionaryService.getCandidateByType(ruleType);
        List<RuleValueDTO> valueList = BeanUtils.copyFields(list,RuleValueDTO.class);
        //已设置的值
        for(RuleValueDTO d:valueList){
            Integer value = Integer.parseInt(d.getValue());
            if(value==3){
                List<RoleDTO> roleList = systemService.getRoleForProject2(null);
                for(RoleDTO role:roleList){
                    RuleDetailDTO detail = new RuleDetailDTO();
                    detail.setId(role.getId());
                    detail.setCode(role.getRoleCode());
                    detail.setValueName(role.getName());
                    d.getValueList().add(detail);
                }
            }
        }
        return valueList;
    }

    private void saveRuleCandidate(String ruleId,SaveRuleDTO dto) throws Exception{
        Example example = new Example(RuleCandidateDO.class);
        example.createCriteria()
                .andCondition("rule_id = ", ruleId);
        ruleCandidateDAO.deleteByExample(example);
        for(Integer type:dto.getRuleCandidateList()){
            RuleCandidateDO candidate = new RuleCandidateDO();
            candidate.initEntity();
            candidate.setDeleted(0);
            candidate.setRuleId(ruleId);
            candidate.setCodeType(type);
            ruleCandidateDAO.insert(candidate);
        }
    }

    private Integer getRuleGroup(Integer ruleType){
        if(ruleType==1 || ruleType==2 || ruleType==5 || ruleType==6){
            return 1;//选择人员组
        }
        if(ruleType==3){
            return 2;//选择审批金额组
        }
        if(ruleType==4){
            return 3;//选择审批请假(出差)组
        }
        return null;
    }

    //（1：企业所有人员，2:部门人员，3：具有角色成员，4：具有权限成员,5:个人,6:群组）',
    private List<RuleValueDTO> getRuleValue(String ruleId,List<RuleValueDTO> valueList) {
        Example example = new Example(RuleDetailDO.class);
        example.createCriteria()
                .andCondition("rule_id = ", ruleId);
        List<RuleDetailDO> list = this.ruleDetailDAO.selectByExample(example);
        for(RuleDetailDO d:list){
            RuleDetailDTO detail = new RuleDetailDTO();
            detail.setId(d.getId());
            if(d.getCodeType()==1){
                //所有人
            }
            if(d.getCodeType()==2){
                //查询部门
            }
            if(d.getCodeType()==3){
                RoleDTO roleDTO = systemService.getRoleByCode(d.getRuleValue());
                detail.setValueName(roleDTO.getName());
            }
            if(d.getCodeType()==4){

            }
            if(d.getCodeType()==5){

            }
            if(d.getCodeType()==6){

            }
            setDetailValue(valueList,d.getCodeType(),detail);
        }
        return null;
    }


    private void setDetailValue(List<RuleValueDTO> valueList,Integer codeType,RuleDetailDTO detail) {
        if(codeType==null){
            return;
        }
        String code = codeType.toString();
        for(RuleValueDTO value:valueList){
            if(value.getValue().equals(code)){
                value.setIsSelect(1);
                value.getValueList().add(detail);
                break;
            }
        }
    }

}
