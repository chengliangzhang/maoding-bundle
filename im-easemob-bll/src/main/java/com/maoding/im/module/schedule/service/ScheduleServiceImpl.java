package com.maoding.im.module.schedule.service;

import com.google.common.collect.Lists;
import com.maoding.core.base.BaseService;
import com.maoding.im.easemob.api.SendMessageApi;
import com.maoding.im.module.message.dao.MessageDAO;
import com.maoding.im.module.message.model.MessageDO;
import com.maoding.im.module.schedule.dao.ScheduleDAO;
import com.maoding.im.module.schedule.dao.ScheduleMemberDAO;
import com.maoding.im.module.schedule.dto.ScheduleDTO;
import com.maoding.im.module.schedule.module.ScheduleMemberDO;
import com.maoding.utils.DateUtils;
import com.maoding.utils.JsonUtils;
import io.swagger.client.model.Msg;
import io.swagger.client.model.MsgContent;
import io.swagger.client.model.UserName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("scheduleService")
public class ScheduleServiceImpl extends BaseService implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Autowired
    private SendMessageApi sendMessageApi;

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private ScheduleMemberDAO scheduleMemberDAO;



    private static String fromUser= "123f9ef123c140fd9b6c7a5123c68e1a";

    private static String targetType = "users";

    @Override
    public void notifySchedule() {
        List<ScheduleDTO> list = scheduleDAO.getSchedule();
        list.stream().forEach(dto->{
            Msg msg = new Msg();
            msg.from(fromUser);
            UserName userName = new UserName();
            userName.addAll(Lists.newArrayList(dto.getUserId()));
            msg.target(userName);
            msg.targetType(targetType);
            MsgContent msgContent = new MsgContent();
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
                //向移动端推送消息
                msgContent.type(MsgContent.TypeEnum.TXT).msg(content);
                msg.setMsg(msgContent);
                sendMessageApi.sendMessage(msg);
            } catch (Exception e) {
                logger.error("handleSendMessage发生异常", e);
            }
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
            scheduleMemberDAO.updateByPrimaryKeySelective(memberDO);
        });
    }
}
