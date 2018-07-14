package com.maoding.yongyoucloud.module.enterprise.service;

import com.maoding.yongyoucloud.module.enterprise.dto.EnterpriseDTO;
import com.maoding.yongyoucloud.module.enterprise.dto.EnterpriseSearchQueryDTO;

import java.util.List;
import java.util.Map;

public interface EnterpriseSearchService {

    Map<String,Object> enterpriseStockHolder(EnterpriseSearchQueryDTO dto) throws Exception;

    Map<String,Object> queryAutoComplete(EnterpriseSearchQueryDTO dto) throws Exception;

    Map<String,Object> queryDetail(EnterpriseSearchQueryDTO dto) throws Exception;

    Map<String,Object> queryFull(EnterpriseSearchQueryDTO dto) throws Exception;

    /**
     * 根据公司id查询客户
     */
    List<EnterpriseDTO> getEnterprise(EnterpriseSearchQueryDTO dto) throws Exception;

    /**
     * 根据enterpriseOrgId查询甲方详情
     */
    Map<String,Object> queryEnterpriseDetail(EnterpriseSearchQueryDTO dto) throws Exception;
}
