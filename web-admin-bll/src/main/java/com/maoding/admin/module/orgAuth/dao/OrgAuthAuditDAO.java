package com.maoding.admin.module.orgAuth.dao;

import com.maoding.admin.module.orgAuth.model.OrgAuthAuditDO;
import com.maoding.admin.module.orgAuth.model.OrgAuthDO;
import com.maoding.core.base.BaseDao;

/**
 * 企业认证
 */
public interface OrgAuthAuditDAO extends BaseDao<OrgAuthAuditDO> {

    void updateStatusByOrgId(String orgId);
}
