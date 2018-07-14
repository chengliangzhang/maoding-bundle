package com.maoding.notify.module.schedule.dao;

import com.maoding.notify.module.schedule.dto.ScheduleDTO;
import com.maoding.notify.module.schedule.model.ScheduleMemberDO;

import java.util.List;

public interface ScheduleDAO {

    List<ScheduleDTO> getSchedule();

    int updateReminderStatus(ScheduleMemberDO scheduleMemberDO);
}
