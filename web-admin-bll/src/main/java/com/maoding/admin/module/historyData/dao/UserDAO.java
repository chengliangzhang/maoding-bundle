package com.maoding.admin.module.historyData.dao;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public interface UserDAO {
    /** 根据用户名和用户所在公司名称获取用户ID */
    String getUserIdByCompanyNameAndUserName(@Param("companyName") String companyName, @Param("userName") String userName);
}
