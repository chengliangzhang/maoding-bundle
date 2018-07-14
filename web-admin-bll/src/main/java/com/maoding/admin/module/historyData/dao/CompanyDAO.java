package com.maoding.admin.module.historyData.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/7/19.
 */
public interface CompanyDAO {
    /** 根据用户名和用户所在公司名获取公司ID */
    String getCompanyIdByCompanyNameAndUserName(@Param("companyName") String companyName, @Param("userName") String userName);
    /** 根据甲方公司名获取公司相关甲方ID */
    String getCompanyIdByCompanyNameForA(@Param("companyName") String companyName, @Param("relateCompanyId") String relateCompanyId);
    /** 根据乙方公司名称获取公司相关乙方ID */
    String getCompanyIdByCompanyNameForB(@Param("companyName") String companyName, @Param("relateCompanyId") String relateCompanyId);
    /** 获取公司具有相关权限的用户ID */
    List<String> listUserIdByCompanyIdAndPermissionId(@Param("companyId") String companyId, @Param("permissionId") String permissionId);
    /** 根据公司ID和用户ID获取雇员ID */
    String getCompanyUserIdByCompanyIdAndUserId(@Param("companyId") String companyId, @Param("userId") String userId);
}
