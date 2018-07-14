package com.maoding.notify;

import com.maoding.notify.module.schedule.service.ScheduleService;
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
    private ScheduleService scheduleService;

    @Scheduled(fixedDelay = 60 * 1000) //每1分钟调用一次
    public void job_ScheduleNotify() throws Exception {
        log.info("------- ------- ------- ScheduleNotify Start ------- ------- -------");
        scheduleService.notifySchedule();
    }
}
