package com.maoding.common.module.companyDisk.dao;


import com.maoding.common.module.companyDisk.model.CompanyDiskDO;
import com.maoding.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wuwq on 2017/05/26.
 */
@Repository
public interface CompanyDiskDAO extends BaseDao<CompanyDiskDO> {

    List<CompanyDiskDO> listAll();

    List<String> listUnInitCompanyId();

    int updateWithOptimisticLock(CompanyDiskDO o);

    /**
     * 根据组织ID统计文档库大小
     */
    Long sumDocmgrSizeByCompanyId(String companyId);

    /**
     * 根据组织ID统计其他文件大小
     */
    Long sumOtherSizeByCompanyId(String companyId);
}
