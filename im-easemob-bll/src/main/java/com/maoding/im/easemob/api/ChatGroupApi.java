package com.maoding.im.easemob.api;

import com.maoding.core.bean.ApiResult;

public interface ChatGroupApi {

    /**
     * 获取群组，参数为空时获取所有群组 <br>
     * GET
     * @param limit  单页数量
     * @param cursor 游标，存在更多记录时产生
     * @return
     */
    ApiResult getChatGroups(Long limit, String cursor);

    /**
     * 获取一个或者多个群组的详情 <br>
     * GET
     * @param groupIds 群组ID数组
     * @return
     */
    ApiResult getChatGroupDetails(String[] groupIds);

    /**
     * 创建一个群组 <br>
     * POST
     * @param payload <code>{"groupname":"testrestgrp12","desc":"server create group","public":true,"maxusers":300,"approval":true,"owner":"jma1","members":["jma2","jma3"]}</code>
     * @return
     */
    ApiResult createChatGroup(Object payload);

    /**
     * 修改群组信息 <br>
     * PUT
     * @param groupId 群组标识
     * @param payload <code>{"groupname":"testrestgrp12",description":"update groupinfo","maxusers":300}</code>
     * @return
     */
    ApiResult modifyChatGroup(String groupId, Object payload);

    /**
     * 删除群组 <br>
     * DELETE
     * @param groupId 群组标识
     * @return
     */
    ApiResult deleteChatGroup(String groupId);

    /**
     * 获取群组所有用户 <br>
     * GET
     * @param groupId 群组标识
     * @return
     */
    ApiResult getChatGroupUsers(String groupId);

    /**
     * 群组加人[单个] <br>
     * POST
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    ApiResult addSingleUserToChatGroup(String groupId, String userId);

    /**
     * 群组加人[批量] <br>
     * POST
     * @param groupId 群组标识
     * @param payload 用户ID或用户名，数组形式
     * @return
     */
    ApiResult addBatchUsersToChatGroup(String groupId, Object payload);

    /**
     * 群组减人[单个] <br>
     * DELETE
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    ApiResult removeSingleUserFromChatGroup(String groupId, String userId);

    /**
     * 群组减人[批量] <br>
     * DELETE
     * @param groupId 群组标识
     * @param userIds 用户ID或用户名，数组形式
     * @return
     */
    ApiResult removeBatchUsersFromChatGroup(String groupId, String[] userIds);

    /**
     * 群组转让 <br>
     * PUT
     * @param groupId 群组标识
     * @param payload 新群主ID或用户名
     * @return
     */
    ApiResult transferChatGroupOwner(String groupId, Object payload);

    /**
     * 查询群组黑名单 <br>
     * GET
     * @param groupId 群组标识
     * @return
     */
    ApiResult getChatGroupBlockUsers(String groupId);

    /**
     * 群组黑名单个添加 <br>
     * POST
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    ApiResult addSingleBlockUserToChatGroup(String groupId, String userId);

    /**
     * 群组黑名单批量添加 <br>
     * POST
     * @param groupId 群组标识
     * @param payload 用户ID或用户名，数组形式
     * @return
     */
    ApiResult addBatchBlockUsersToChatGroup(String groupId, Object payload);

    /**
     * 群组黑名单单个删除 <br>
     * DELETE
     * @param groupId 群组标识
     * @param userId  用户ID或用户名
     * @return
     */
    ApiResult removeSingleBlockUserFromChatGroup(String groupId, String userId);

    /**
     * 群组黑名单批量删除 <br>
     * DELETE
     * @param groupId 群组标识
     * @param userIds 用户ID或用户名，数组形式
     * @return
     */
    ApiResult removeBatchBlockUsersFromChatGroup(String groupId, String[] userIds);
}
