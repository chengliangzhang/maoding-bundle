package com.maoding.im.easemob.api;

import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.comm.ApiResultHandler;
import com.maoding.im.easemob.comm.TokenUtils;
import com.maoding.im.config.EasemobConfig;
import io.swagger.client.api.ChatHistoryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatMessageApi")
public class ChatMessageApiImpl implements ChatMessageApi {

    @Autowired
    private ApiResultHandler apiResultHandler;

    @Autowired
    private ChatHistoryApi chatHistoryApi;

    @Override
    public ApiResult exportChatMessages(final Long limit, final String cursor, final String query) {
        return apiResultHandler.handle(() -> chatHistoryApi.orgNameAppNameChatmessagesGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), query, limit + "", cursor));
    }
}
