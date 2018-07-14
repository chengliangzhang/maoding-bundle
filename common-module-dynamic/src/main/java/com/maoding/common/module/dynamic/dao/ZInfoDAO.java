package com.maoding.common.module.dynamic.dao;

import com.maoding.common.module.dynamic.model.*;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Chengliang.zhang on 2017/6/26.
 */
public interface ZInfoDAO {
    String getCompanyUserIdByCompanyIdAndUserId(@Param("companyId") String companyId, @Param("userId") String userId);
    CompanyUserEntity getCompanyUserByCompanyUserId(String id);
}
