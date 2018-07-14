package com.maoding.admin.module.historyData.dao;

import com.maoding.admin.module.historyData.dto.MemberQueryDTO;
import com.maoding.admin.module.historyData.model.ProjectMemberDO;
import com.maoding.core.base.BaseDao;

/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public interface ProjectMemberDAO extends BaseDao<ProjectMemberDO> {
    /** 根据查询条件获取项目成员实体 */
    ProjectMemberDO getProjectMember(MemberQueryDTO query);
}
