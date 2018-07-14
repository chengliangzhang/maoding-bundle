package com.maoding.corp.module.corpserver.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.corp.module.corpserver.model.MyTaskDO;

/**
 * Created by Wuwq on 2017/05/25.
 */
public interface MyTaskDAO extends BaseDao<MyTaskDO> {

    MyTaskDO getMyTaskByProcessNodeId(String processNodeId);

    MyTaskDO getPrincipalTaskByProjectTaskId(String projectTaskId);

    int updateMyTaskAsFinished(String myTaskId);

    int updateMyTaskAsActived(String myTaskId);

    int updateMyTaskAsInvalid(String myTaskId);
}
