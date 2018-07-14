package com.maoding.im.easemob.api;

import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.comm.ApiResultHandler;
import com.maoding.im.easemob.comm.TokenUtils;
import com.maoding.im.config.EasemobConfig;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sendMessageApi")
public class SendMessageApiImpl implements SendMessageApi {

    @Autowired
    private ApiResultHandler apiResultHandler;

    @Autowired
    private MessagesApi messagesApi;

    @Override
    public ApiResult sendMessage(final Object payload) {
        return apiResultHandler.handle(() -> messagesApi.orgNameAppNameMessagesPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), (Msg) payload));
    }
}
