package com.maoding.im.listener;

import com.maoding.hxIm.dto.ImQueueDTO;
import com.maoding.im.config.ActiveMQConfig;
import com.maoding.im.constDefine.ImDestination;
import com.maoding.im.module.imQueue.service.ImQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ImJMSListener {

    @Autowired
    private ImQueueService imQueueService;

    @JmsListener(destination = ImDestination.ACCOUNT, containerFactory = ActiveMQConfig.LISTENER_CONTAINER_QUEUE)
    public void onAccountMessage(ImQueueDTO queue) {
        imQueueService.handleAccountMessage(queue);
    }

    @JmsListener(destination = ImDestination.GROUP, containerFactory = ActiveMQConfig.LISTENER_CONTAINER_QUEUE)
    public void onGroupMessage(ImQueueDTO queue) {
        imQueueService.handleGroupMessage(queue);
    }

    @JmsListener(destination = ImDestination.GROUP_MEMBER, containerFactory = ActiveMQConfig.LISTENER_CONTAINER_QUEUE)
    public void onGroupMemberMessage(ImQueueDTO queue) {
        imQueueService.handleGroupMemberMessage(queue);
    }

    @JmsListener(destination = ImDestination.SEND_MESSAGE, containerFactory = ActiveMQConfig.LISTENER_CONTAINER_QUEUE)
    public void onSendMessage(ImQueueDTO queue) {
        imQueueService.handleSendMessage(queue);
    }
}
