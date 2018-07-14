package com.maoding.im.module.schedule.module;


import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_web_schedule_member")
public class ScheduleMemberDO extends BaseEntity {

    private String scheduleId;

    private String memberId;

    private Integer status;

    private String refuseReason;

    private Integer reminderTime;

    private Integer deleted;

    private Integer isReminder;//是否已经提醒:1=提醒，其他：未提醒

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId == null ? null : scheduleId.trim();
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason == null ? null : refuseReason.trim();
    }

    public Integer getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Integer reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getIsReminder() {
        return isReminder;
    }

    public void setIsReminder(Integer isReminder) {
        this.isReminder = isReminder;
    }
}