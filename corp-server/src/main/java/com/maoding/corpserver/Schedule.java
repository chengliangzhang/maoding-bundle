package com.maoding.corpserver;

import com.maoding.corp.module.corpserver.service.SyncCompanyServise;
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
    private SyncCompanyServise syncCompanyServise;

    //同步所有数据
    @Scheduled(initialDelay = 0, fixedDelay = 600 * 1000)
    public void job_SyncCompanyToRedis() throws InterruptedException {
        log.info("-------------- Schedule Job SyncCompanyToRedis Start --------------");
        syncCompanyServise.syncToRedis();
        log.info("-------------- Schedule Job SyncCompanyToRedis End --------------");
    }
}
