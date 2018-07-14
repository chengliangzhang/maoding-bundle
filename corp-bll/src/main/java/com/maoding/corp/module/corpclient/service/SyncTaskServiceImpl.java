package com.maoding.corp.module.corpclient.service;

import com.google.common.collect.Maps;
import com.maoding.constDefine.corp.SyncCmd;
import com.maoding.constDefine.corp.SyncStatus;
import com.maoding.constDefine.corp.TaskStatus;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpclient.dao.SyncTaskDAO;
import com.maoding.corp.module.corpclient.model.SyncTaskDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Wuwq on 2017/2/14.
 */
@Service("syncTaskService")
public class SyncTaskServiceImpl extends BaseService implements SyncTaskService {

    private static final Logger log = LoggerFactory.getLogger(SyncTaskServiceImpl.class);

    @Autowired
    private SyncTaskDAO syncTaskDAO;

    @Autowired
    private CoService coService;

    @Autowired
    private SowService sowService;

    /**
     * 同步协同占用
     */
    @Override
    public ApiResult syncCorpSizeByCompanyId(String companyId) {
        try {
            //协同占用空间同步
            ApiResult ar = sowService.getCorpSizeByCompanyId(companyId);
            if (ar.isSuccessful()) {
                Long corpSize = Math.round((double) ar.getData());
                Map<String, Object> param = Maps.newHashMap();
                param.put("companyId", companyId);
                param.put("corpSize", corpSize.toString());
                coService.updateCorpSizeOnCompanyDisk(param);
                return ApiResult.success(null, null);
            }
        } catch (Exception e) {
            log.error(String.format("组织：%s 同步协同占用发生异常（syncCorpSizeByCompanyId）", companyId), e);
        }
        return ApiResult.failed(null, null);
    }

    /**
     * 跳过一个任务，并把该任务标记为同步成功
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ApiResult skipOneTask(SyncTaskDO task) {
        task.setTaskStatus(TaskStatus.SkipByDuplicate);
        task.setSyncStatus(SyncStatus.SyncSuccess);
        task.setLastEntry(LocalDateTime.now());
        task.resetUpdateDate();

        if (syncTaskDAO.updateWithOptimisticLock(task) > 0)
            return ApiResult.success(null, task);
        return ApiResult.failed(null);
    }


    /**
     * 同步一个任务
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ApiResult runOneTask(String syncTaskId) {
        //重新从数据库加载
        SyncTaskDO task = syncTaskDAO.selectByPrimaryKey(syncTaskId);
        task.setLastEntry(LocalDateTime.now());
        task.setTaskStatus(TaskStatus.Running);
        task.resetUpdateDate();

        String logPrefix = getLogPrefix(task);
        int syncStatus = task.getSyncStatus();

        //筛选待同步、待重试
        if (syncStatus == SyncStatus.WaitSync || syncStatus == SyncStatus.WaitRetry) {
            //重试次数+1
            if (syncStatus == SyncStatus.WaitRetry) {
                task.setRetryTimes(task.getRetryTimes() + 1);
                log.info("{}：开始执行，重试（{}）", logPrefix, task.getRetryTimes());
            } else {
                log.info("{}：开始执行", logPrefix);
            }

            ApiResult apiResult = ApiResult.failed(null);
            try {
                if (SyncCmd.CU.equalsIgnoreCase(task.getSyncCmd())) {
                    apiResult = sowService.pushUserByCompanyId(task.getCompanyId(), task.getSyncPoint());

                } else if (SyncCmd.CD.equalsIgnoreCase(task.getSyncCmd())) {
                    syncCorpSizeByCompanyId(task.getCompanyId());
                    //无论如何，同步协同空间都算成功，避免影响正常逻辑
                    apiResult = ApiResult.success(null);

                } else if (SyncCmd.PALL.equalsIgnoreCase(task.getSyncCmd())) {
                    apiResult = sowService.setProject(task.getProjectId(), task.getSyncPoint(), true);

                } else if (SyncCmd.PU.equalsIgnoreCase(task.getSyncCmd())) {
                    apiResult = sowService.setProject(task.getProjectId(), task.getSyncPoint(), false);

                } else if (SyncCmd.PT.equalsIgnoreCase(task.getSyncCmd())) {
                    apiResult = sowService.setProjectNodes(task.getProjectId(), task.getSyncPoint(), task.getProjectPhaseId());
                }

            } catch (Exception e) {
                log.error(String.format("%s：执行同步发生异常", logPrefix), e);
            }

            if (apiResult.isSuccessful()) {
                task.setTaskStatus(TaskStatus.Finished);
                task.setSyncStatus(SyncStatus.SyncSuccess);
                if (syncTaskDAO.updateWithOptimisticLock(task) > 0)
                    log.info("{}：同步成功，状态更新为：已结束", logPrefix);
                return ApiResult.success(null, task);
            }

            if (task.getRetryTimes() >= 3) {
                task.setTaskStatus(TaskStatus.SkipByMaxRetry);
                task.setSyncStatus(SyncStatus.SyncFailed);
                if (syncTaskDAO.updateWithOptimisticLock(task) > 0) {
                    log.info("{}：同步失败，重试次数超出限制，状态更新为：已跳过", logPrefix);
                    return ApiResult.success(null, task);
                }
            } else {
                task.setTaskStatus(TaskStatus.WaitRuning);
                task.setSyncStatus(SyncStatus.WaitRetry);
                if (syncTaskDAO.updateWithOptimisticLock(task) > 0) {
                    log.info("{}：同步失败，待重试", logPrefix);
                    return ApiResult.success(null, task);
                }
            }
        } else {
            task.setTaskStatus(TaskStatus.Finished);
            if (syncTaskDAO.updateWithOptimisticLock(task) > 0) {
                log.info("{}：状态更新为：已结束", logPrefix);
                return ApiResult.success(null, task);
            }
        }
        return ApiResult.failed(null, task);
    }

    /**
     * 获取日志前缀
     */
    private String getLogPrefix(SyncTaskDO task) {
        String logPrefix;
        if (SyncCmd.CU.equalsIgnoreCase(task.getSyncCmd()) || SyncCmd.CD.equalsIgnoreCase(task.getSyncCmd())) {
            logPrefix = String.format("端点 %s 组织 %s 任务（%s - t：%s）", task.getCorpEndpoint(), task.getCompanyId(), task.getSyncCmd(), task.getId());
        } else if (SyncCmd.PALL.equalsIgnoreCase(task.getSyncCmd()) || SyncCmd.PU.equalsIgnoreCase(task.getSyncCmd())) {
            logPrefix = String.format("端点 %s 项目 %s 任务（%s - t：%s）", task.getCorpEndpoint(), task.getProjectId(), task.getSyncCmd(), task.getId());
        } else {
            logPrefix = String.format("端点 %s 项目 %s 阶段：%s 任务（%s - t：%s）", task.getCorpEndpoint(), task.getProjectId(), task.getSyncCmd(), task.getProjectPhaseId(), task.getId());
        }
        return logPrefix;
    }
}
