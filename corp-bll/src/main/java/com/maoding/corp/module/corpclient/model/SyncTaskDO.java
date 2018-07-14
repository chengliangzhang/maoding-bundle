package com.maoding.corp.module.corpclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maoding.core.base.BaseEntity;
import com.maoding.utils.NumberUtils;
import com.maoding.utils.StringUtils;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Wuwq on 2017/2/14.
 */
@Table(name = "maoding_corp_sync_task")
public class SyncTaskDO extends BaseEntity {

    /**
     * 协同端id
     */
    private String corpEndpoint;
    /**
     * 组织Id
     */
    private String companyId;
    /**
     * 项目Id
     */
    private String projectId;
    /**
     * 项目阶段Id
     */
    private String projectPhaseId;
    /**
     * 要同步的时间点
     */
    private LocalDateTime syncPoint;
    /**
     * 最后开始执行时间，用来判断超时
     */
    private LocalDateTime lastEntry;
    /**
     * 同步优先级（值越小越高）
     */
    private Integer syncPriority;
    /**
     * 同步指令（（CU、PU、PT0、PT1、PT2））
     */
    private String syncCmd;

    /**
     * 同步状态（0：待同步，1：待重试，2：同步成功，3：同步失败）
     */
    private Integer syncStatus;
    /**
     * 状态(0：待执行，1：执行中，2：已结束）
     */
    private Integer taskStatus;
    /**
     * 当前重试次数
     */
    private Integer retryTimes;
    /**
     * 版本控制(乐观锁）
     */
    @JsonIgnore
    private Long upVersion;

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCorpEndpoint() {
        return corpEndpoint;
    }

    public void setCorpEndpoint(String corpEndpoint) {
        this.corpEndpoint = corpEndpoint;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectPhaseId() {
        return projectPhaseId;
    }

    public void setProjectPhaseId(String projectPhaseId) {
        this.projectPhaseId = projectPhaseId;
    }

    public LocalDateTime getSyncPoint() {
        return syncPoint;
    }

    public void setSyncPoint(LocalDateTime syncPoint) {
        this.syncPoint = syncPoint;
    }

    public LocalDateTime getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(LocalDateTime lastEntry) {
        this.lastEntry = lastEntry;
    }

    public Integer getSyncPriority() {
        return syncPriority;
    }

    public void setSyncPriority(Integer syncPriority) {
        this.syncPriority = syncPriority;
    }

    public String getSyncCmd() {
        return syncCmd;
    }

    public void setSyncCmd(String syncCmd) {
        this.syncCmd = syncCmd;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getUpVersion() {
        return upVersion;
    }

    public void setUpVersion(Long upVersion) {
        this.upVersion = upVersion;
    }
}
