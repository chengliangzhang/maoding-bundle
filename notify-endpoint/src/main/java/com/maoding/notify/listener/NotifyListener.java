package com.maoding.notify.listener;

import com.maoding.notify.config.ActiveMQConfig;
import com.maoding.notify.constDefine.NotifyDestination;
import com.maoding.notify.module.message.dto.SendMessageDataDTO;
import com.maoding.notify.verticle.ActiveMQVerticleForApp;
import com.maoding.notify.verticle.ActiveMQVerticleForWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotifyListener {

    @Autowired
    private ActiveMQVerticleForWeb activeMQVerticleForWeb;

    @Autowired
    private ActiveMQVerticleForApp activeMQVerticleForApp;

    @JmsListener(destination = NotifyDestination.WEB, containerFactory =  ActiveMQConfig.LISTENER_CONTAINER_QUEUE)
    public void onNotifyWeb(SendMessageDataDTO msg) {
        activeMQVerticleForWeb.handleMsg(msg);
    }

    @JmsListener(destination = NotifyDestination.APP, containerFactory =  ActiveMQConfig.LISTENER_CONTAINER_QUEUE)
    public void onNotifyApp(SendMessageDataDTO msg) {
        activeMQVerticleForApp.handleMsg(msg);
    }

}
