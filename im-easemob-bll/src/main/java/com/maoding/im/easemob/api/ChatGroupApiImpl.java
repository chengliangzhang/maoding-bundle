package com.maoding.im.easemob.api;

import com.maoding.core.bean.ApiResult;
import com.maoding.im.easemob.comm.ApiResultHandler;
import com.maoding.im.easemob.comm.TokenUtils;
import com.maoding.im.config.EasemobConfig;
import io.swagger.client.StringUtil;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatGroupApi")
public class ChatGroupApiImpl implements ChatGroupApi {

    @Autowired
    private ApiResultHandler apiResultHandler;

    @Autowired
    private GroupsApi groupsApi;

    @Override
    public ApiResult getChatGroups(final Long limit, final String cursor) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), limit + "", cursor));
    }

    @Override
    public ApiResult getChatGroupDetails(final String[] groupIds) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdsGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), StringUtil.join(groupIds, ",")));
    }

    @Override
    public ApiResult createChatGroup(final Object payload) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), (Group) payload));
    }

    @Override
    public ApiResult modifyChatGroup(final String groupId, final Object payload) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdPut(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, (ModifyGroup) payload));
    }

    @Override
    public ApiResult deleteChatGroup(final String groupId) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId));
    }

    @Override
    public ApiResult getChatGroupUsers(final String groupId) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdUsersGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId));
    }

    @Override
    public ApiResult addSingleUserToChatGroup(final String groupId, final String userId) {
        final UserNames userNames = new UserNames();
        UserName userList = new UserName();
        userList.add(userId);
        userNames.usernames(userList);
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, userNames));
    }

    @Override
    public ApiResult addBatchUsersToChatGroup(final String groupId, final Object payload) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, (UserNames) payload));
    }

    @Override
    public ApiResult removeSingleUserFromChatGroup(final String groupId, final String userId) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdUsersUsernameDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, userId));
    }

    @Override
    public ApiResult removeBatchUsersFromChatGroup(final String groupId, final String[] userIds) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdUsersMembersDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, StringUtil.join(userIds, ",")));
    }

    @Override
    public ApiResult transferChatGroupOwner(final String groupId, final Object payload) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupidPut(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, (NewOwner) payload));
    }

    @Override
    public ApiResult getChatGroupBlockUsers(final String groupId) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersGet(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId));
    }

    @Override
    public ApiResult addSingleBlockUserToChatGroup(final String groupId, final String userId) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamePost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, userId));
    }

    @Override
    public ApiResult addBatchBlockUsersToChatGroup(final String groupId, final Object payload) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, (UserNames) payload));
    }

    @Override
    public ApiResult removeSingleBlockUserFromChatGroup(final String groupId, final String userId) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernameDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, userId));
    }

    @Override
    public ApiResult removeBatchBlockUsersFromChatGroup(final String groupId, final String[] userIds) {
        return apiResultHandler.handle(() -> groupsApi.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamesDelete(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, TokenUtils.getAccessToken(), groupId, StringUtil.join(userIds, ",")));
    }
}
