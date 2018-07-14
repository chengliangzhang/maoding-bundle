package com.maoding.im.module.imAccount.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.im.module.imAccount.dto.ImAccountDTO;

import java.util.List;

public interface ImAccountService {

    /** 创建环信账号 **/
    ApiResult create(ImAccountDTO dto);

    /** 批量创建环信账号 **/
    ApiResult createBatch(List<ImAccountDTO> list);

    /** 修改环信昵称 **/
    ApiResult modifyNickname(ImAccountDTO dto);

    /** 重置环信密码 **/
    ApiResult modifyPassword(ImAccountDTO dto);

    /** 删除环信账号 **/
    ApiResult delete(String accountId);

    ApiResult deleteIMUserBatch();

    /**
     * 环信账号同步
     */
    void AccountSynIm();
}
