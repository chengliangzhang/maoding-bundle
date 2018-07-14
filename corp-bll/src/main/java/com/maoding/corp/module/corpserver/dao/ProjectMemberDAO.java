package com.maoding.corp.module.corpserver.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.corp.module.corpserver.dto.CoProjectProcessNodeDTO;
import com.maoding.corp.module.corpserver.model.ProjectMemberDO;
import com.maoding.corp.module.corpserver.model.ProjectProcessNodeDO;

import java.util.List;

/**
 * Created by Wuwq on 2017/05/25.
 */
public interface ProjectMemberDAO extends BaseDao<ProjectMemberDO> {
     List<ProjectMemberDO> listMemberByTaskId(String taskId);

    /**
     * 获取任务负责人
     */
    ProjectMemberDO getTaskPrincipal(String taskId);
}
