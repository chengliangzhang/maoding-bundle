package com.maoding.corp.module.corpclient.service;

public interface SyncTaskDispatchService {
    /**
     * 启动时把任务状态为“执行中”的重置为“等待执行”
     */
    void resetTaskStatusOnStartup();


    /**
     * 执行指定端点下同步任务
     */
    void runTasksByEndpoint(String endpoint) throws Exception;
}
