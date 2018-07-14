package com.maoding.yongyoucloud.module.enterprise.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.yongyoucloud.module.enterprise.dto.OperateLinkmanDTO;

public interface EnterpriseLinkmanService {

    ApiResult saveLinkman(OperateLinkmanDTO dto);

    ApiResult deleteLinkman(OperateLinkmanDTO dto);

}
