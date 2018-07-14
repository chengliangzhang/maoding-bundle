package com.maoding.yongyoucloud.module.enterprise.service;

import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.utils.StringUtils;
import com.maoding.yongyoucloud.module.enterprise.dao.EnterpriseOrgLinkmanDAO;
import com.maoding.yongyoucloud.module.enterprise.dao.EnterpriseProjectLinkmanDAO;
import com.maoding.yongyoucloud.module.enterprise.dto.OperateLinkmanDTO;
import com.maoding.yongyoucloud.module.enterprise.model.EnterpriseOrgLinkmanDO;
import com.maoding.yongyoucloud.module.enterprise.model.EnterpriseProjectLinkmanDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service("enterpriseLinkmanService")
public class EnterpriseLinkmanServiceImpl extends BaseService implements EnterpriseLinkmanService {

    @Autowired
    private EnterpriseOrgLinkmanDAO enterpriseOrgLinkmanDAO;

    @Autowired
    private EnterpriseProjectLinkmanDAO enterpriseProjectLinkmanDAO;

    @Override
    public ApiResult saveLinkman(OperateLinkmanDTO dto) {
         EnterpriseOrgLinkmanDO entity = new EnterpriseOrgLinkmanDO();
        BeanUtils.copyProperties(dto,entity);
        if(StringUtils.isNullOrEmpty(dto.getId())) {
            entity.setCreateBy(dto.getAccountId());
            entity.initEntity();
            enterpriseOrgLinkmanDAO.insert(entity);
            //保存联系人与项目的关联信息
            EnterpriseProjectLinkmanDO projectLinkman = new EnterpriseProjectLinkmanDO();
            projectLinkman.initEntity();
            projectLinkman.setCreateBy(dto.getAccountId());
            projectLinkman.setProjectId(dto.getProjectId());
            projectLinkman.setEnterpriseOrgId(dto.getEnterpriseOrgId());
            projectLinkman.setLinkmanId(entity.getId());
            projectLinkman.setPosition(dto.getPosition());
            enterpriseProjectLinkmanDAO.insert(projectLinkman);
        }else {//修改
            entity.setUpdateBy(dto.getAccountId());
            enterpriseOrgLinkmanDAO.updateByPrimaryKeySelective(entity);
            if(!StringUtils.isNullOrEmpty(dto.getProjectId()) && !StringUtils.isNullOrEmpty(dto.getProjectLinkmanId())){
                EnterpriseProjectLinkmanDO projectLinkman = enterpriseProjectLinkmanDAO.selectByPrimaryKey(dto.getProjectLinkmanId());
                if(projectLinkman!=null){
                    projectLinkman.setProjectId(dto.getProjectId());
                    projectLinkman.setPosition(dto.getPosition());
                    enterpriseProjectLinkmanDAO.updateByPrimaryKey(projectLinkman);
                }
            }
        }
        return ApiResult.success(entity);
    }

    @Override
    public ApiResult deleteLinkman(OperateLinkmanDTO dto) {
        int i = enterpriseProjectLinkmanDAO.deleteByPrimaryKey(dto.getProjectLinkmanId());
        Example example = new Example(EnterpriseProjectLinkmanDO.class);
        example.createCriteria()
                .andCondition("linkman_id = ", dto.getId());
        if (enterpriseProjectLinkmanDAO.selectByExample(example).size()==0){
            enterpriseOrgLinkmanDAO.deleteByPrimaryKey(dto.getId());
        }
        if(i == 1){
            return ApiResult.success(null);
        }
        return ApiResult.failed("删除失败");
    }
}
