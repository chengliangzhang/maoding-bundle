package com.maoding.corpclient;

import com.maoding.corp.config.CorpClientConfig;
import com.maoding.corp.module.corpclient.service.CoService;
import com.maoding.corp.module.corpclient.service.SyncService;
import com.maoding.corp.module.corpclient.service.SyncTaskDispatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Wuwq on 2017/2/9.
 * 同步的定时任务
 */
@Component
public class Schedule {
    private static final Logger log = LoggerFactory.getLogger(Schedule.class);

    @Autowired
    private SyncService syncService;

    @Autowired
    private SyncTaskDispatchService syncTaskDispatchService;

    @Autowired
    private CoService coService;

    //同步所有数据（每天凌晨1点）
    //@Scheduled(initialDelay = 0, fixedDelay = 86400 * 1000)
    @Scheduled(cron = "0 0 1 * * *")
    public void job_SyncAll() throws Exception {
        if (!Application.EnableSchedule)
            return;
        log.info("------- ------- ------- SyncAll Start ------- ------- -------");
        coService.syncAllByEndpoint(CorpClientConfig.EndPoint);
    }

    //拉取变更
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 15 * 1000)
    public void job_PullChanges() throws Exception {
        if (!Application.EnableSchedule)
            return;
        log.info("------- ------- ------- PullChanges Start ------- ------- -------");
        syncService.pullChanges(CorpClientConfig.EndPoint);
    }

    //执行同步任务
    @Scheduled(initialDelay = 20 * 1000, fixedDelay = 10 * 1000)
    public void job_RunSyncTask() throws Exception {
        if (!Application.EnableSchedule)
            return;
        log.info("------- ------- ------- RunSyncTask Start ------- ------- -------");
        syncTaskDispatchService.runTasksByEndpoint(CorpClientConfig.EndPoint);
    }
}
