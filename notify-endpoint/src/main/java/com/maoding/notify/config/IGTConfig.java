package com.maoding.notify.config;

import com.maoding.notify.module.msgPusher.MsgPusher;
import com.maoding.notify.module.msgPusher.MsgPusherFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Idccapp21 on 2016/11/29.
 */

@Configuration
public class IGTConfig {
    @Value("${IGT.appId}")
    private String appId;

    @Value("${IGT.appKey}")
    private String appKey;

    @Value("${IGT.appToken}")
    private String appToken;

    @Value("${IGT.host}")
    private String host;

    @Bean(name="msgpusherFactory")
    public MsgPusherFactory msgpusherFactory(){
        MsgPusherFactory msgpusherFactory = MsgPusherFactory.getInstance();
        msgpusherFactory.setAppid(appId);
        msgpusherFactory.setAppkey(appKey);
        msgpusherFactory.setAppToken(appToken);
        msgpusherFactory.setHost(host);
        return msgpusherFactory;
    }

    @Bean(name="msgpusher")
    public MsgPusher msgpusher(MsgPusherFactory msgpusherFactory){
      return   msgpusherFactory.defaultMsgpusher();
    }
}
