package com.maoding.im.easemob.api;

import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.comm.ApiResultHandler;
import com.maoding.im.easemob.comm.TokenUtils;
import com.maoding.im.config.EasemobConfig;
import io.swagger.client.StringUtil;
import io.swagger.client.api.ChatRoomsApi;
import io.swagger.client.model.Chatroom;
import io.swagger.client.model.ModifyChatroom;
import io.swagger.client.model.UserNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatRoomApi")
public class ChatRoomApiImpl implements ChatRoomApi {

    @Autowired
    private ApiResultHandler apiResultHandler;

    @Autowired
    private ChatRoomsApi chatRoomsApi;

    @Override
    public ApiResult createChatRoom(final Object payload) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), (Chatroom) payload));
    }

    @Override
    public ApiResult modifyChatRoom(final String roomId, final Object payload) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsChatroomIdPut(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), roomId, (ModifyChatroom) payload));
    }

    @Override
    public ApiResult deleteChatRoom(final String roomId) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsChatroomIdDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), roomId));
    }

    @Override
    public ApiResult getAllChatRooms() {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken()));
    }

    @Override
    public ApiResult getChatRoomDetail(final String roomId) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsChatroomIdGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), roomId));
    }

    @Override
    public ApiResult addSingleUserToChatRoom(final String roomId, final String userName) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersUsernamePost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), roomId, userName));
    }

    @Override
    public ApiResult addBatchUsersToChatRoom(final String roomId, final Object payload) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), roomId, (UserNames) payload));
    }

    @Override
    public ApiResult removeSingleUserFromChatRoom(final String roomId, final String userName) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersUsernameDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), roomId, userName));
    }

    @Override
    public ApiResult removeBatchUsersFromChatRoom(final String roomId, final String[] userNames) {
        return apiResultHandler.handle(() -> chatRoomsApi.orgNameAppNameChatroomsChatroomIdUsersUsernamesDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), roomId, StringUtil.join(userNames, ",")));
    }
}
