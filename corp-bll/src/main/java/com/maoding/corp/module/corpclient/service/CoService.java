package com.maoding.corp.module.corpclient.service;

import com.maoding.core.bean.ApiResult;

import java.util.Map;

/**
 * Created by Wuwq on 2017/4/24.
 */
public interface CoService {
    /**
     * 登录接口
     */
    ApiResult login(Map<String, Object> param) throws Exception;

    /**
     * 根据组织ID获取公司网盘容量信息
     */
    ApiResult getCompanyDiskInfo(Map<String, Object> param) throws Exception;

    /**
     * 根据组织ID更新协同占用空间
     */
    ApiResult updateCorpSizeOnCompanyDisk(Map<String, Object> param) throws Exception;

    /**
     * 设置节点完成状态
     */
    ApiResult handleMyTaskByProjectNodeId(Map<String, Object> param) throws Exception;

    /**
     * 同步端点下所有内容
     */
    ApiResult syncAllByEndpoint(String endpoint) throws Exception;

}
