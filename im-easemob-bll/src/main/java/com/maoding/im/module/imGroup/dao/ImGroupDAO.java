package com.maoding.im.module.imGroup.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.im.module.imGroup.dto.ImGroupDTO;
import com.maoding.im.module.imGroup.dto.ImGroupMemberDTO;
import com.maoding.im.module.imGroup.model.ImGroupDO;
import com.maoding.im.module.imGroup.model.ImGroupMemberDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImGroupDAO extends BaseDao<ImGroupDO> {
    int updateWithOptimisticLock(ImGroupDO o);

    /**
     * 同步所以组织的群组
     */
    List<ImGroupDTO> selectAllCompany();
    List<ImGroupMemberDTO> selectGroupMemberByOrgId(@Param("orgId") String orgId);

    String getCompanyUserId(ImGroupMemberDO member);
}
