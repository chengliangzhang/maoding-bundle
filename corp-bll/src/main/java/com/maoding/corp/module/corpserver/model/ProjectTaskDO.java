package com.maoding.corp.module.corpserver.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;
import java.util.Date;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskEntity
 * 类描述：项目任务
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
@Table(name = "maoding_web_project_task")
public class ProjectTaskDO extends BaseEntity {

    /**
     * 公司Id
     */
    private String companyId;

    /**
     * 项目Id
     */
    private String projectId;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 任务父Id
     */
    private String taskPid;


    /**
     * 任务类型
     */
    private int taskType;

    /**
     * 签发次数级别
     */
    private int taskLevel;

    /**
     * 任务状态 0生效，1不生效
     */
    private String taskStatus;

    /**
     * 备注
     */
    private String taskRemark;
    /**
     * 排序
     */
    private int seq;
    /**
     * 任务完整路径id-id
     */
    private String taskPath;


    /**
     * 完成时间
     */
    private Date completeDate;

    /**
     * 是否是经营任务（1：经营任务，0：是经营任务，但是可以进行生产，或许直接是生产任务）
     */
    private int isOperaterTask;

    private String orgId;//部门id

    /**
     * 被修改记录的id，用于修改任务，新增一条未被发布的数据，该字段记录被修改记录的id
     */
    private String beModifyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getTaskPid() {
        return taskPid;
    }

    public void setTaskPid(String taskPid) {
        this.taskPid = taskPid == null ? null : taskPid.trim();
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.trim();
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark == null ? null : taskRemark.trim();
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTaskPath() {
        return taskPath;
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath == null ? null : taskPath.trim();
    }

    public int getIsOperaterTask() {
        return isOperaterTask;
    }

    public void setIsOperaterTask(int isOperaterTask) {
        this.isOperaterTask = isOperaterTask;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBeModifyId() {
        return beModifyId;
    }

    public void setBeModifyId(String beModifyId) {
        this.beModifyId = beModifyId;
    }
}