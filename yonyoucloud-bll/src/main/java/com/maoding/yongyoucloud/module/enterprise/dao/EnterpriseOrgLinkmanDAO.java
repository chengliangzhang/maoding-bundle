package com.maoding.yongyoucloud.module.enterprise.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.yongyoucloud.module.enterprise.dto.EnterpriseProjectLinkmanDTO;
import com.maoding.yongyoucloud.module.enterprise.dto.EnterpriseSearchQueryDTO;
import com.maoding.yongyoucloud.module.enterprise.model.EnterpriseOrgLinkmanDO;

import java.util.List;

public interface EnterpriseOrgLinkmanDAO extends BaseDao<EnterpriseOrgLinkmanDO> {

    List<EnterpriseProjectLinkmanDTO> getLinkman(EnterpriseSearchQueryDTO dto);

    int getProjectEditRole(EnterpriseSearchQueryDTO dto);
    int getProjectEditRole2(EnterpriseSearchQueryDTO dto);
}
