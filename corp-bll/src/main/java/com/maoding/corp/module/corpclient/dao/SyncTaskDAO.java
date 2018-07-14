package com.maoding.corp.module.corpclient.dao;

import com.maoding.core.base.BaseDao;
import com.maoding.corp.module.corpclient.model.SyncTaskDO;

import java.util.List;

/**
 * Created by Wuwq on 2017/2/14.
 */
public interface SyncTaskDAO extends BaseDao<SyncTaskDO> {

    /**
     * 查询未完成的任务（待执行、执行中）
     */
    List<SyncTaskDO> listUnfinishedTask(String endpoint);

    /**
     * 查询待执行的任务
     */
    List<SyncTaskDO> listWaitRuningTask(String endpoint);

    /**
     * 查询执行中的任务
     */
    List<SyncTaskDO> listRuningTask(String endpoint);

    int updateRunngingAsWaitRunningStatus();

    int updateWithOptimisticLock(SyncTaskDO o);
}
