package com.maoding.common.module.companyDisk.service;


import com.google.common.collect.Lists;
import com.maoding.common.module.companyDisk.dao.CompanyDiskDAO;
import com.maoding.common.module.companyDisk.model.CompanyDiskDO;
import com.maoding.constDefine.companyDisk.FileChangeType;
import com.maoding.constDefine.companyDisk.FileSizeSumType;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.utils.SegmentLock;
import com.maoding.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("companyDiskService")
public class CompanyDiskServiceImpl extends BaseService implements CompanyDiskService {

    private static final Logger log = LoggerFactory.getLogger(CompanyDiskServiceImpl.class);
    private static final Long INIT_DISK_SIZE = 5L * 1024L * 1024L * 1024L;
    private static final SegmentLock<String> segmentLock = new SegmentLock<>(64, true);

    @Autowired
    private CompanyDiskDAO companyDiskDao;

    private ApiResult initOne(String companyId) {
        CompanyDiskDO cd = new CompanyDiskDO();
        cd.initEntity();
        cd.setId(companyId);
        cd.setCompanyId(companyId);
        cd.setTotalSize(INIT_DISK_SIZE);
        cd.setCorpSize(0L);
        cd.setDocmgrSize(0L);
        cd.setOtherSize(0L);
        cd.setUpVersion(0L);
        cd.setCorpOnCloud(true);
        cd.recalcFreeSize();

        if (companyDiskDao.insert(cd) > 0)
            return ApiResult.success(null, cd);

        return ApiResult.failed(null, null);
    }

    /**
     * 初始化组织空间
     */
    @Override
    public ApiResult initDisk(String companyId) {
        if (companyDiskDao.selectByPrimaryKey(companyId) == null)
            return initOne(companyId);

        return ApiResult.failed("", null);
    }

    /**
     * 初始化所有组织空间
     */
    @Override
    public ApiResult initAllDisk() {
        List<String> ids = companyDiskDao.listUnInitCompanyId();
        List<CompanyDiskDO> cds = Lists.newArrayList();
        if (ids != null && ids.size() > 0) {
            //只添加未初始化的公司
            for (String id : ids) {
                ApiResult ar = initDisk(id);
                if (ar.isSuccessful())
                    cds.add((CompanyDiskDO) ar.getData());
            }
        }

        return ApiResult.success(null, cds);
    }

    /**
     * 当添加文件时重新计算容量（线程安全）
     */
    @Override
    public ApiResult recalcSizeOnFileAdded(String companyId, FileSizeSumType sumType, Long fileSize) {
        ApiResult ar = ApiResult.failed(null, null);
        int maxRetryTimes = 3;
        //重试是为了可能发生的乐观锁并发问题
        for (int i = 0; i < maxRetryTimes; i++) {
            if (i > 0)
                log.error("尝试更新网盘信息失败（companyId:{}），准备重试第 {} 次", companyId, i);
            segmentLock.lock(companyId);
            try {
                ar = ((CompanyDiskService) AopContextCurrentProxy()).recalcSizeOnFileChangedUnsafely(companyId, sumType, FileChangeType.ADDED, fileSize);
            } catch (Exception ex) {
                //这里捕捉异常是为了不影响正常业务
                i = maxRetryTimes;
                log.error("当添加文件时重新计算容量时发生异常", ex);
            } finally {
                segmentLock.unlock(companyId);
            }
            if (ar.isSuccessful())
                i = maxRetryTimes;
        }
        return ar;
    }

    /**
     * 当删除文件时重新计算容量（线程安全）
     */
    @Override
    public ApiResult recalcSizeOnFileRemoved(String companyId, FileSizeSumType sumType, Long fileSize) {
        ApiResult ar = ApiResult.failed(null, null);
        int maxRetryTimes = 3;
        //重试是为了可能发生的乐观锁并发问题
        for (int i = 0; i < maxRetryTimes; i++) {
            if (i > 0)
                log.error("尝试更新网盘信息失败（companyId:{}），准备重试第 {} 次", companyId, i);
            segmentLock.lock(companyId);
            try {
                ar = ((CompanyDiskService) AopContextCurrentProxy()).recalcSizeOnFileChangedUnsafely(companyId, sumType, FileChangeType.REMOVED, fileSize);
            } catch (Exception ex) {
                //这里捕捉异常是为了不影响正常业务
                i = maxRetryTimes;
                log.error("当删除文件时重新计算容量时发生异常", ex);
            } finally {
                segmentLock.unlock(companyId);
            }
            if (ar.isSuccessful())
                i = maxRetryTimes;
        }
        return ar;
    }

    /**
     * 根据组织ID统计指定类型文件大小（线程安全）
     */
    @Override
    public ApiResult recalcSize(String companyId, FileSizeSumType sumType) {
        ApiResult ar;
        segmentLock.lock(companyId);
        try {
            ar = ((CompanyDiskService) AopContextCurrentProxy()).recalcSizeUnsafely(companyId, sumType);
        } finally {
            segmentLock.unlock(companyId);
        }
        return ar;
    }


    /**
     * 当文档库变更时重新计算容量（非线程安全）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public ApiResult recalcSizeOnFileChangedUnsafely(String companyId, FileSizeSumType sumType, FileChangeType changeType, Long fileSize) {
        if (StringUtils.isBlank(companyId))
            return ApiResult.failed("未指定组织ID", null);

        CompanyDiskDO cd = companyDiskDao.selectByPrimaryKey(companyId);
        if (cd == null) {
            log.info("找不到指定组织的网盘信息（companyId:{}），执行初始化", companyId);

            ApiResult ar = initOne(companyId);
            if (!ar.isSuccessful())
                return ar;
            cd = (CompanyDiskDO) ar.getData();
        }

        if (sumType.equals(FileSizeSumType.DOCMGR)) {

            if (changeType.equals(FileChangeType.ADDED))
                cd.setDocmgrSize(cd.getDocmgrSize() + fileSize);
            else
                cd.setDocmgrSize(cd.getDocmgrSize() - fileSize);

        } else if (sumType.equals(FileSizeSumType.OTHER)) {

            if (changeType.equals(FileChangeType.ADDED))
                cd.setOtherSize(cd.getOtherSize() + fileSize);
            else
                cd.setOtherSize(cd.getOtherSize() - fileSize);

        } else {
            throw new UnsupportedOperationException("非法的文件统计类型");
        }

        cd.recalcFreeSize();
        cd.resetUpdateDate();

        int updateCount = companyDiskDao.updateWithOptimisticLock(cd);
        if (updateCount > 0)
            return ApiResult.success(null, cd);

        return ApiResult.failed(null, cd);
    }

    /**
     * 根据组织ID重新统计指定类型文件大小（非线程安全）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public ApiResult recalcSizeUnsafely(String companyId, FileSizeSumType sumType) {
        if (StringUtils.isBlank(companyId))
            return ApiResult.failed("未指定组织ID", null);

        CompanyDiskDO cd = companyDiskDao.selectByPrimaryKey(companyId);
        if (cd == null) {
            log.info("找不到指定组织的网盘信息（companyId:{}），执行初始化", companyId);

            ApiResult ar = initOne(companyId);
            if (!ar.isSuccessful())
                return ar;
            cd = (CompanyDiskDO) ar.getData();
        }

        if (sumType == null) {
            cd.setDocmgrSize(companyDiskDao.sumDocmgrSizeByCompanyId(companyId));
            cd.setOtherSize(companyDiskDao.sumOtherSizeByCompanyId(companyId));
        } else if (sumType.equals(FileSizeSumType.DOCMGR)) {
            cd.setDocmgrSize(companyDiskDao.sumDocmgrSizeByCompanyId(companyId));
        } else if (sumType.equals(FileSizeSumType.OTHER)) {
            cd.setOtherSize(companyDiskDao.sumOtherSizeByCompanyId(companyId));
        } else {
            throw new UnsupportedOperationException("非法的文件统计类型");
        }
        cd.recalcFreeSize();
        cd.resetUpdateDate();

        int updateCount = companyDiskDao.updateWithOptimisticLock(cd);
        if (updateCount > 0)
            return ApiResult.success(null, cd);

        return ApiResult.failed(null, cd);
    }

    /**
     * 根据组织ID更新协同占用空间（线程安全）
     */
    @Override
    public ApiResult updateCorpSize(String companyId, Long corpSize) {
        ApiResult ar;
        segmentLock.lock(companyId);
        try {
            ar = ((CompanyDiskService) AopContextCurrentProxy()).updateCorpSizeUnsafely(companyId, corpSize);
        } finally {
            segmentLock.unlock(companyId);
        }
        return ar;
    }

    /**
     * 根据组织ID更新协同占用空间（非线程安全）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    @Override
    public ApiResult updateCorpSizeUnsafely(String companyId, Long corpSize) {
        if (StringUtils.isBlank(companyId))
            return ApiResult.failed("未指定组织ID", null);

        if (corpSize == null)
            return ApiResult.failed("未指定协同占用空间", null);

        CompanyDiskDO cd = companyDiskDao.selectByPrimaryKey(companyId);
        if (cd == null) {
            log.info("找不到指定组织的网盘信息（companyId:{}），执行初始化", companyId);

            ApiResult ar = initOne(companyId);
            if (!ar.isSuccessful())
                return ar;
            cd = (CompanyDiskDO) ar.getData();
        }

        cd.setCorpSize(corpSize);
        cd.recalcFreeSize();
        cd.resetUpdateDate();

        int updateCount = companyDiskDao.updateWithOptimisticLock(cd);
        if (updateCount > 0) {
            log.info("组织 {} 更新协同占用空间：{}({})", companyId, corpSize, StringUtils.getSize(corpSize));
            return ApiResult.success(null, cd);
        }

        return ApiResult.failed(null, cd);
    }

    /**
     * 根据组织ID获取公司网盘容量信息
     */
    @Override
    public ApiResult getCompanyDiskInfo(String companyId) {
        if (StringUtils.isBlank(companyId))
            return ApiResult.failed("未指定组织ID", null);

        CompanyDiskDO cd = companyDiskDao.selectByPrimaryKey(companyId);
        if (cd == null) {
            log.info("找不到指定组织的网盘信息（companyId:{}），执行初始化", companyId);

            ApiResult ar = initOne(companyId);
            if (!ar.isSuccessful())
                return ar;
            cd = (CompanyDiskDO) ar.getData();
        }

        //重新计算
        cd.recalcFreeSize();

        log.info("组织 {} 获取网盘信息", companyId);
        return ApiResult.success("获取公司网盘信息成功！", cd);
    }

    /**
     * 根据组织ID切换协同部署方式
     */
    @Override
    public ApiResult switchCorpDeployType(String companyId, Boolean corpOnCloud) {
        if (StringUtils.isBlank(companyId))
            return ApiResult.failed("未指定组织ID", null);

        CompanyDiskDO cd = companyDiskDao.selectByPrimaryKey(companyId);
        if (cd == null) {
            log.info("找不到指定组织的网盘信息（companyId:{}），执行初始化", companyId);

            ApiResult ar = initOne(companyId);
            if (!ar.isSuccessful())
                return ar;
            cd = (CompanyDiskDO) ar.getData();
        }

        if(corpOnCloud==null)
            cd.setCorpOnCloud(!cd.getCorpOnCloud());
        else
            cd.setCorpOnCloud(corpOnCloud);

        cd.recalcFreeSize();
        cd.resetUpdateDate();

        int updateCount = companyDiskDao.updateWithOptimisticLock(cd);
        if (updateCount > 0) {
            log.info("组织 {} 更新切换协同部署方式为：{}", companyId,cd.getCorpOnCloud()?"云端部署":"本地部署");
            return ApiResult.success(null, cd);
        }

        return ApiResult.failed(null, cd);
    }


}
