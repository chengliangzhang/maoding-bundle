package com.maoding.corpclient;

import com.maoding.corp.module.corpclient.service.SyncTaskDispatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Idccapp21 on 2017/2/20.
 */

@Component
public class StartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);
    @Autowired
    private SyncTaskDispatchService syncTaskDispatchService;

    @Override
    public void run(String... strings) throws Exception {
        log.info("------- ------- ------- ResetTaskStatus Start ------- ------- -------");
        syncTaskDispatchService.resetTaskStatusOnStartup();

        if (!Application.EnableSchedule)
            return;

        //log.info("-------------- SyncAll Start --------------");
        //syncTaskService.syncAll(CorpClientConfig.EndPoint);
    }

}
