package com.maoding.notify.verticle;

import com.maoding.notify.constDefine.NotifyDestination;
import com.maoding.notify.constDefine.NotifyType;
import com.maoding.notify.module.message.dto.SendMessageDataDTO;
import com.maoding.notify.module.msgPusher.MsgPusher;
import com.maoding.notify.utils.JsonUtils;
import com.maoding.utils.StringUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuwq on 2016/10/14.
 */
@Component
public class ActiveMQVerticleForApp extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQVerticleForApp.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MsgPusher msgpusher;

//    /*创建 ActiveMq 监听*/
//    private void createActiveMQConsumer() {
//        try {
//            Connection connection = connectionFactory.createConnection();
//            connection.start();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Destination destination = session.createQueue(NotifyDestination.APP);
//            MessageConsumer consumer = session.createConsumer(destination);
//            consumer.setMessageListener(msg -> {
//                try {
//                    MapMessage objectMessage = (MapMessage) msg;
//                    Map<String, Object> resultMap = (Map<String, Object>) objectMessage.getObject("messageEntity");
//                    String msgType = (String) resultMap.get("messageType");
//                    if (!StringUtils.isNullOrEmpty(msgType)) {
//                        switch (msgType) {
//                            case NotifyType.APP_NOTICE:
//                                msgpusher.pushRoleMsgToUserList((List<String>) resultMap.get("receiverList"), "Message", (String)resultMap.get("content"),msgType);
//                                break;
//                            case NotifyType.APP_ROLE:
//                            case NotifyType.APP_PROJECT:
//                                msgpusher.pushRoleMsgToUserList((List<String>) resultMap.get("receiverList"),"Permission", "Operator",msgType);
//                                break;
//                            /*case NotifyType.APP_USER_MESSAGE:
//                                this.producerService.sendMessage(this.sendMessageDestination,resultMap);
//                                break;*/
//                        }
//                    }
//                } catch (JMSException e) {
//                    logger.error("ActiveMQ Received Exception:{}", e);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        } catch (Exception e) {
//            logger.error("ActiveMQ Connection Exception:{}", e);
//        }
//    }



    public void handleMsg(SendMessageDataDTO m) {
        try {
            if (!StringUtils.isNullOrEmpty(m.getMessageType())) {
                switch (m.getMessageType()) {
                    case NotifyType.APP_NOTICE:
                        m.setTitle("通知公告");
                        break;
                    case NotifyType.APP_MESSAGE:
                        m.setTitle("卯丁秘书");
                        break;
                    case NotifyType.MAODING_CIRCLE_TYPE:
                        m.setTitle("讨论区·"+m.getOtherContent().get("projectName"));
                        break;
                    case NotifyType.ORG_TYPE:
                        m.setTitle("卯丁通知");
                        break;
                    default:return;
                }
                msgpusher.pushRoleMsgToUserList(m);
            }
        } catch (Exception e) {
            logger.error("消息推送APP端异常，ActiveMQ Received Exception:{}", e);
        }
    }

    @Override
    public void start() throws Exception {
        //createActiveMQConsumer();
    }
}
