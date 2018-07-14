package com.maoding.notify.verticle;

import com.maoding.notify.config.AppConfig;
import com.maoding.notify.constDefine.NotifyType;
import com.maoding.notify.module.message.dto.SendMessageDataDTO;
import com.maoding.notify.utils.JsonUtils;
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
public class ActiveMQVerticleForWeb extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMQVerticleForWeb.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ConnectionFactory connectionFactory;

    /*创建 ActiveMq 监听*/
    private void createActiveMQConsumer() {
//        try {
//            Connection connection = connectionFactory.createConnection();
//            connection.start();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Destination destination = session.createQueue(NotifyDestination.WEB);
//            MessageConsumer consumer = session.createConsumer(destination);
//            consumer.setMessageListener(msg -> {
//                try {
//                    MessageDto m = getMessage(msg);
//                    String url;
//                    switch (m.getMessageType()) {
//                        case NotifyType.WEB_USER_MESSAGE:
//                            url = appConfig.getClientSubUrl_TopicSpec() + "." + m.getReceiver();
//                            vertx.eventBus().publish(url, new JsonObject(JsonUtils.obj2json(m)));
//                            break;
//                        case NotifyType.WEB_NOTICE:
//                            List<String> receiverList = m.getReceiverList();
//                            for (String r : receiverList) {
//                                url = appConfig.getClientSubUrl_TopicSpec() + "." + r;
//                                vertx.eventBus().publish(url, new JsonObject(JsonUtils.obj2json(m)));
//                            }
//                            break;
//                    }
//
//
//                    /*vertx.eventBus().send(url, new JsonObject(JsonUtils.obj2json(m)), ar -> {
//                        if (ar.succeeded()) {
//                            logger.debug("ActiveMQ Send Ok");
//                        } else {
//                            logger.debug("ActiveMQ Send Failed");
//                        }
//                    });*/
//
//                } catch (JMSException e) {
//                    logger.error("ActiveMQ Received Exception:{}", e);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        } catch (Exception e) {
//            logger.error("ActiveMQ Connection Exception:{}", e);
//        }
    }


    private SendMessageDataDTO getMessage(Message msg) throws JMSException {
        Map<String, Object> map = (Map<String, Object>) ((MapMessage) msg).getObject("messageEntity");
        logger.debug("ActiveMQ Receive:" + map);
        SendMessageDataDTO m = new SendMessageDataDTO();
        String messageType = (String) map.getOrDefault("messageType", null);
        m.setMessageType(messageType);

        switch (messageType) {
            case NotifyType.WEB_USER_MESSAGE:
                m.setReceiver((String) map.getOrDefault("receiver", null));
                m.setContent((String) map.getOrDefault("msg", null));
                break;
            case NotifyType.WEB_NOTICE:
                m.setContent((String) map.getOrDefault("noticeTitle", null));
                m.setReceiverList((List<String>) map.getOrDefault("receiverList", null));
                break;
        }

        return m;
    }


   public void handleMsg(SendMessageDataDTO m) {
       try {
           String url;
           switch (m.getMessageType()) {
               case NotifyType.WEB_USER_MESSAGE:
                   url = appConfig.getClientSubUrl_TopicSpec() + "." + m.getReceiver();
                   vertx.eventBus().publish(url, new JsonObject(JsonUtils.obj2json(m)));
                   break;
               case NotifyType.WEB_NOTICE:
                   List<String> receiverList = m.getReceiverList();
                   for (String r : receiverList) {
                       url = appConfig.getClientSubUrl_TopicSpec() + "." + r;
                       vertx.eventBus().publish(url, new JsonObject(JsonUtils.obj2json(m)));
                   }
                   break;
           }

       } catch (JMSException e) {
           logger.error("ActiveMQ Received Exception:{}", e);
       } catch (Exception e) {
           logger.error("推送消息-》web端异常:{}", e);
       }
   }

    @Override
    public void start() throws Exception {
        createActiveMQConsumer();
    }
}
