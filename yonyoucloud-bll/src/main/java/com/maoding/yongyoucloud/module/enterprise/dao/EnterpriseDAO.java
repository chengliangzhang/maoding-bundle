package com.maoding.yongyoucloud.module.enterprise.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.yongyoucloud.module.enterprise.dto.EnterpriseDTO;
import com.maoding.yongyoucloud.module.enterprise.dto.EnterpriseSearchQueryDTO;
import com.maoding.yongyoucloud.module.enterprise.model.EnterpriseDO;

import java.util.List;

public interface EnterpriseDAO extends BaseDao<EnterpriseDO> {

    List<EnterpriseDTO> getEnterprise(EnterpriseSearchQueryDTO dto);

    EnterpriseDO getEnterpriseByName(EnterpriseSearchQueryDTO dto);
}
