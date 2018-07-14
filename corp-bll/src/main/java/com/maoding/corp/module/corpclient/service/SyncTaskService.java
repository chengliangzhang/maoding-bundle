package com.maoding.corp.module.corpclient.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpclient.model.SyncTaskDO;

/**
 * Created by Wuwq on 2017/2/14.
 */
public interface SyncTaskService {

    /**
     * 跳过一个任务，并把该任务标记为同步成功
     */
    ApiResult skipOneTask(SyncTaskDO task);

    /**
     * 同步一个任务
     */
    ApiResult runOneTask(String syncTaskId);

    /**
     * 同步协同占用
     */
    ApiResult syncCorpSizeByCompanyId(String companyId);
}
