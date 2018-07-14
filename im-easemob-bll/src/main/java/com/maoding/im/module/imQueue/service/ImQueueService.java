package com.maoding.im.module.imQueue.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.hxIm.dto.ImQueueDTO;
import com.maoding.im.module.imQueue.model.ImQueueDO;

public interface ImQueueService {
    /** 处理IM账号消息 */
    ApiResult handleAccountMessage(ImQueueDTO queue);

    /** 处理IM群组消息 */
    ApiResult handleGroupMessage(ImQueueDTO queue);

    /** 处理IM群组成员消息 */
    ApiResult handleGroupMemberMessage(ImQueueDTO queue);

    /** 处理IM发送消息 */
    ApiResult handleSendMessage(ImQueueDTO queue);

    /** 拉取并推送修复消息 */
    void pullAndProcessImQueue(int countPerTime);

    /** 处理修复消息 */
    void processImQueue(ImQueueDO queue);
}
