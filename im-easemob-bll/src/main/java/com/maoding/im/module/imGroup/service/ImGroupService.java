package com.maoding.im.module.imGroup.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.im.module.imGroup.dto.ImGroupDTO;

public interface ImGroupService {
    /** 创建环信群组 **/
    ApiResult create(ImGroupDTO dto);

    /** 删除群 */
    ApiResult delete(String groupNo);

    /** 修改环信群组名 **/
    ApiResult modifyGroupName(ImGroupDTO dto);


    /** 移交环信群主 **/
    ApiResult transferGroupOwner(ImGroupDTO dto);

    /** 增加群成员 **/
    ApiResult addMembers(ImGroupDTO dto);

    /** 移除群成员 **/
    ApiResult deleteMembers(ImGroupDTO dto);

    /**
     * 同步群组
     */
    void groupSyncIm();
}
