package com.maoding.corp.module.corpclient.service;

import com.maoding.core.bean.ApiResult;

import java.time.LocalDateTime;

/**
 * Created by Wuwq on 2017/06/09.
 * 面向协同推送的服务
 */
public interface SowService {
    /**
     * 通过组织Id推送项目信息
     */
    ApiResult pushProjectByCompanyId2(String companyId, LocalDateTime syncDate) throws Exception;

    /**
     * 根据组织Id推送人员信息
     */
    ApiResult pushUserByCompanyId(String companyId, LocalDateTime syncDate) throws Exception;

    /**
     * 设置项目
     */
    ApiResult setProject(String projectId, LocalDateTime syncDate, Boolean pushAllNodes) throws Exception;

    /**
     * 设置项目节点（含任务成员状态信息）
     * @param projectPhaseId 设为NULL则表示所有阶段
     * @throws Exception
     */
    ApiResult setProjectNodes(String projectId, LocalDateTime syncDate, String projectPhaseId) throws Exception;

    /**
     * 发布项目（同步任务文件夹）
     */
    ApiResult publicProject(String projectId) throws Exception;

    /**
     * 根据组织ID获取协同占用空间
     */
    ApiResult getCorpSizeByCompanyId(String companyId) throws Exception;
}
