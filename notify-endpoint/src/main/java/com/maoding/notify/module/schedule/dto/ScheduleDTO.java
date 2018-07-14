package com.maoding.notify.module.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ScheduleDTO {

    private String id;

    private String content;

    private String remark;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date startTime;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date endTime;

    private Integer scheduleType;

    private String userId;

    private String scheduleMemberId;

    private String companyId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(Integer scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScheduleMemberId() {
        return scheduleMemberId;
    }

    public void setScheduleMemberId(String scheduleMemberId) {
        this.scheduleMemberId = scheduleMemberId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
