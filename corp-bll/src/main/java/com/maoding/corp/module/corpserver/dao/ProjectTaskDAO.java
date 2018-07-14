package com.maoding.corp.module.corpserver.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.corp.module.corpserver.model.ProjectTaskDO;

/**
 * Created by Wuwq on 2017/05/25.
 */
public interface ProjectTaskDAO extends BaseDao<ProjectTaskDO> {

    int updateProcessTaskAsActived(String projectTaskId);
}
