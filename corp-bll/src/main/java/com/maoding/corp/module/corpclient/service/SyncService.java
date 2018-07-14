package com.maoding.corp.module.corpclient.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpclient.dto.PushResult;

import java.util.Map;

/**
 * Created by Wuwq on 2017/4/24.
 */
public interface SyncService {
    /**
     * 根据端点拉取组织变更
     */
    void pullChanges_C(String endpoint);

    /**
     * 根据端点和项目ID拉取变更
     */
    void pullChanges_P_ID(String endpoint, String projectId);

    /**
     * 拉取变更
     */
    void pullChanges(String corpEndpoint) throws Exception;

    /**
     * 从业务系统拉取数据（GET）
     */
    ApiResult pullFromCorpServer(String url) throws Exception;

    /**
     * 从业务系统拉取数据（POST）
     */
    ApiResult pullFromCorpServer(Map<String, Object> param, String url) throws Exception;

    /**
     * 推送到对方协同服务端
     */
    PushResult postToSOWServer(Object param, String url) throws Exception;
}
