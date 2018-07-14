package com.maoding.im.module.schedule.dao;

import com.maoding.im.module.schedule.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleDAO {

    List<ScheduleDTO> getSchedule();
}
