package com.maoding.admin.module.orgAuth.dao;

import com.maoding.admin.module.orgAuth.dto.OrgAuthAttachDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthQueryDTO;
import com.maoding.admin.module.orgAuth.model.OrgAuthDO;
import com.maoding.core.base.BaseDao;

import java.util.List;

/**
 * Created by Wuwq on 2017/05/31.
 */
public interface OrgAuthDAO extends BaseDao<OrgAuthDO> {
    /**
     * 方法：根据查询条件查找审核信息列表
     * 作者：ZCL
     * 日期：2017/7/11
     */
    List<OrgAuthDTO> listOrgAuthenticationInfo(OrgAuthQueryDTO query);

    /**
     * 方法：根据组织ID查找附件表
     * 作者：ZCL
     * 日期：2017/7/11
     */
    List<OrgAuthAttachDTO> listOrgAuthAttach(List<String> idList);
}
