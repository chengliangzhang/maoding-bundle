package com.maoding.im.easemob.api;

import com.maoding.core.bean.ApiResult;

public interface ChatRoomApi {
    /**
     * 创建聊天室 <br>
     * POST
     * @param payload <code>{name":"testchatroom","description":"server create chatroom","maxusers":300,"owner":"jma1","members":["jma2","jma3"]}</code>
     * @return
     */
    ApiResult createChatRoom(Object payload);

    /**
     * 修改聊天室信息 <br>
     * PUT
     * @param roomId  聊天室标识
     * @param payload <code>{"name":"test chatroom","description":
     *                "update chatroominfo","maxusers":200}
     * @return
     */
    ApiResult modifyChatRoom(String roomId, Object payload);

    /**
     * 删除聊天室 <br>
     * DELETE
     * @param roomId 聊天室标识
     * @return
     */
    ApiResult deleteChatRoom(String roomId);

    /**
     * 获取app中所有的聊天室 <br>
     * GET
     * @return
     */
    ApiResult getAllChatRooms();

    /**
     * 获取一个聊天室详情 <br>
     * GET
     * @param roomId 聊天室标识
     * @return
     */
    ApiResult getChatRoomDetail(String roomId);

    /**
     * 聊天室成员添加[单个] <br>
     * POST
     * @param roomId   聊天室标识
     * @param userName 用户ID或用户名
     * @return
     */
    ApiResult addSingleUserToChatRoom(String roomId, String userName);

    /**
     * 聊天室成员添加[批量] <br>
     * POST
     * @param roomId  聊天室标识
     * @param payload 用户ID或用户名，数组形式
     * @return
     */
    ApiResult addBatchUsersToChatRoom(String roomId, Object payload);

    /**
     * 聊天室成员删除[单个] <br>
     * DELETE
     * @param roomId   聊天室标识
     * @param userName 用户ID或用户名
     * @return
     */
    ApiResult removeSingleUserFromChatRoom(String roomId, String userName);

    /**
     * 聊天室成员删除[批量] <br>
     * DELETE
     * @param roomId    聊天室标识
     * @param userNames 用户ID或用户名，数组形式
     * @return
     */
    ApiResult removeBatchUsersFromChatRoom(String roomId, String[] userNames);
}
