package com.maoding.common.module.companyDisk.service;


import com.maoding.constDefine.companyDisk.FileChangeType;
import com.maoding.constDefine.companyDisk.FileSizeSumType;
import com.maoding.core.bean.ApiResult;


public interface CompanyDiskService {

    /**
     * 初始化组织空间
     */
    ApiResult initDisk(String companyId);

    /**
     * 初始化所有组织空间
     */
    ApiResult initAllDisk();

    /**
     * 当文档库添加文件时重新计算容量（线程安全）
     */
    ApiResult recalcSizeOnFileAdded(String companyId, FileSizeSumType sumType, Long fileSize);

    /**
     * 当删除文件时重新计算容量（线程安全）
     */
    ApiResult recalcSizeOnFileRemoved(String companyId, FileSizeSumType sumType, Long fileSize);

    /**
     * 当文档库变更时重新计算容量（非线程安全）
     */
    ApiResult recalcSizeOnFileChangedUnsafely(String companyId, FileSizeSumType sumType, FileChangeType changeType, Long fileSize);

    /**
     * 根据组织ID统计指定类型文件大小（线程安全）
     */
    ApiResult recalcSize(String companyId, FileSizeSumType sumType);

    /**
     * 根据组织ID统计指定类型文件大小（非线程安全）
     */
    ApiResult recalcSizeUnsafely(String companyId, FileSizeSumType sumType);

    /**
     * 根据组织ID更新协同占用空间（线程安全）
     */
    ApiResult updateCorpSize(String companyId, Long corpSize);

    /**
     * 根据组织ID更新协同占用空间（非线程安全）
     */
    ApiResult updateCorpSizeUnsafely(String companyId, Long corpSize);

    /**
     * 根据组织ID获取公司网盘容量信息
     */
    ApiResult getCompanyDiskInfo(String companyId);

    /**
     * 根据组织ID切换协同部署方式
     */
    ApiResult switchCorpDeployType(String companyId, Boolean corpOnCloud);
}
