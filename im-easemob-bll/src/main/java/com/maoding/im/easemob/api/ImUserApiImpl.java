package com.maoding.im.easemob.api;


import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.comm.ApiResultHandler;
import com.maoding.im.easemob.comm.TokenUtils;
import com.maoding.im.config.EasemobConfig;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.NewPassword;
import io.swagger.client.model.Nickname;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.UserNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imUserApi")
public class ImUserApiImpl implements ImUserApi {

    @Autowired
    private ApiResultHandler apiResultHandler;

    @Autowired
    private UsersApi usersApi;


    @Override
    public ApiResult createNewIMUserSingle(final Object payload) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, (RegisterUsers) payload, TokenUtils.getAccessToken()));
    }

    @Override
    public ApiResult createNewIMUserBatch(final Object payload) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, (RegisterUsers) payload, TokenUtils.getAccessToken()));
    }

    @Override
    public ApiResult getIMUserByUserName(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult getIMUsersBatch(final Long limit, final String cursor) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), limit + "", cursor));
    }

    @Override
    public ApiResult deleteIMUserByUserName(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult deleteIMUserBatch(final Long limit, final String cursor) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), limit + "", cursor));
    }

    @Override
    public ApiResult modifyIMUserPasswordWithAdminToken(final String userName, final Object payload) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernamePasswordPut(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, userName, (NewPassword) payload, TokenUtils.getAccessToken()));
    }

    @Override
    public ApiResult modifyIMUserNickNameWithAdminToken(final String userName, final Object payload) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernamePut(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, userName, (Nickname) payload, TokenUtils.getAccessToken()));
    }

    @Override
    public ApiResult addFriendSingle(final String userName, final String friendName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName, friendName));
    }

    @Override
    public ApiResult deleteFriendSingle(final String userName, final String friendName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName, friendName));
    }

    @Override
    public ApiResult getFriends(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersOwnerUsernameContactsUsersGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult getBlackList(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersOwnerUsernameBlocksUsersGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult addToBlackList(final String userName, final Object payload) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersOwnerUsernameBlocksUsersPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName, (UserNames) payload));
    }

    @Override
    public ApiResult removeFromBlackList(final String userName, final String blackListName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersOwnerUsernameBlocksUsersBlockedUsernameDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName, blackListName));
    }

    @Override
    public ApiResult getIMUserStatus(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameStatusGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult getOfflineMsgCount(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersOwnerUsernameOfflineMsgCountGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult getSpecifiedOfflineMsgStatus(final String userName, final String msgId) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameOfflineMsgStatusMsgIdGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName, msgId));
    }

    @Override
    public ApiResult deactivateIMUser(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameDeactivatePost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult activateIMUser(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameActivatePost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult disconnectIMUser(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameDisconnectGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult getIMUserAllChatGroups(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameJoinedChatgroupsGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }

    @Override
    public ApiResult getIMUserAllChatRooms(final String userName) {
        return apiResultHandler.handle(() -> usersApi.orgNameAppNameUsersUsernameJoinedChatroomsGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), userName));
    }
}
