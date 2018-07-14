package com.maoding.corp.module.corpserver.service;


import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpserver.dto.CoCompanyDTO;
import com.maoding.corp.module.corpserver.dto.CoProjectPhaseDTO;
import com.maoding.corp.module.corpserver.dto.CoUserDTO;
import com.maoding.corp.module.corpserver.dto.ProjectDTO;

import java.util.List;
import java.util.Map;


public interface CollaborationService {


    List<CoCompanyDTO> listCompanyByIds(List<String> companyIds);


    /**
     * 根据组织Id获取项目列表
     */
    List<ProjectDTO> listProjectByCompanyId(String companyId, String syncDate) throws Exception;

    /**
     * 根据projectId获取项目信息
     */
    ProjectDTO getProjectById(String projectId);


    /**
     * 获取项目节点（含任务成员状态信息）
     */
    List<CoProjectPhaseDTO> listNode(String projectId) throws Exception;


    /**
     * 登录接口
     */
    ApiResult login(Map<String, Object> map) throws Exception;

    /**
     * 推送同步指令强制同步一个组织下面的所有
     */
    void pushSyncCMD_SyncAllByCompany(String syncCompanyId);

    /**
     * 推送同步指令强制同步一个Endpoint下面的所有
     */
    void pushSyncCMD_SyncAllByEndpoint(String endpoint);

    /**
     * 推送同步指令PT（触发条件：阶段变动（PT0）、签发变动（PT1）、生产变动（PT2））
     */
    void pushSyncCMD_PT(String projectId, String taskPath, String syncCmd);

    /**
     * 完成我的任务
     */
    ApiResult finishMyTask(String processNodeId);

    /**
     * 激活我的任务
     */
    ApiResult activeMyTask(String processNodeId);

    /**
     * 根据组织Id获取用户列表
     */
    List<CoUserDTO> listUserByCompanyId(String companyId);
}
