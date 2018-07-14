package com.maoding.notify.module.schedule.service;

import com.maoding.core.base.BaseService;
import com.maoding.notify.constDefine.NotifyType;
import com.maoding.notify.module.message.dao.MessageDAO;
import com.maoding.notify.module.message.dto.SendMessageDataDTO;
import com.maoding.notify.module.message.model.MessageDO;
import com.maoding.notify.module.schedule.dao.ScheduleDAO;
import com.maoding.notify.module.schedule.dto.ScheduleDTO;
import com.maoding.notify.module.schedule.model.ScheduleMemberDO;
import com.maoding.notify.verticle.ActiveMQVerticleForApp;
import com.maoding.utils.DateUtils;
import com.maoding.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("scheduleService")
public class ScheduleServiceImpl extends BaseService implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private ActiveMQVerticleForApp activeMQVerticleForApp;

    @Override
    public void notifySchedule() {
        List<ScheduleDTO> list = scheduleDAO.getSchedule();
        list.stream().forEach(dto->{

            //组装存储数据的消息信息
            String content = "";
            Map<String,String> map = new HashMap<>();
            MessageDO message = new MessageDO();
            if(dto.getScheduleType()==1){
                message.setMessageType(709);
                content = "你将有一个日程为“"+dto.getContent()+"”,开始时间为："+dto.getStartTime();
            }else {
                message.setMessageType(710);
                content = "你将有一个会议为“"+dto.getContent()+"”,开始时间为："+dto.getStartTime();
            }
            map.put("scheduleContent",dto.getContent());
            map.put("startTime1", DateUtils.date2Str(dto.getStartTime(),DateUtils.time_sdf_slash));
            map.put("endTime1", DateUtils.date2Str(dto.getEndTime(),DateUtils.time_sdf_slash));
            try {
                message.setMessageContent(JsonUtils.obj2json(map));
            } catch (Exception e) {
                logger.error("handleSendMessage发生异常", e);
            }

            //通过个推发送消息
            SendMessageDataDTO notifyMsg = new SendMessageDataDTO();
            notifyMsg.setMessageType(NotifyType.APP_MESSAGE);
            notifyMsg.setReceiverList(Arrays.asList(dto.getUserId()));
            notifyMsg.setContent(content);
            activeMQVerticleForApp.handleMsg(notifyMsg);

            //插入数据到Message表中
            message.initEntity();
            message.setUserId(dto.getUserId());
            message.setCompanyId(dto.getCompanyId());
            message.setSendCompanyId(dto.getCompanyId());
            message.setTargetId(dto.getId());
            message.setStatus("0");
            messageDAO.insert(message);
            //把当前记录设置为已经推送消息的状态
            ScheduleMemberDO memberDO = new ScheduleMemberDO();
            memberDO.setId(dto.getScheduleMemberId());
            memberDO.setIsReminder(1);
            scheduleDAO.updateReminderStatus(memberDO);
        });
    }
}
