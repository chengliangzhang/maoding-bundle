package com.maoding.corp.module.corpserver.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpserver.dto.SyncCompanyDTO_Create;
import com.maoding.corp.module.corpserver.dto.SyncCompanyDTO_Update;

/**
 * Created by Wuwq on 2017/2/10.
 */
public interface SyncCompanyServise {

    ApiResult create(SyncCompanyDTO_Create dto);

    ApiResult update(SyncCompanyDTO_Update dto);

    ApiResult delete(String corpEndpoint, String companyId);

    ApiResult delete(String id);

    ApiResult select(String corpEndpoint);

    ApiResult syncToRedis();
}
