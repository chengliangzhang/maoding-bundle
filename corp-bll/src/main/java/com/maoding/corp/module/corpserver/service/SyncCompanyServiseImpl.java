package com.maoding.corp.module.corpserver.service;

import com.google.common.collect.Lists;
import com.maoding.constDefine.corp.RKey;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpserver.dao.SyncCompanyDAO;
import com.maoding.corp.module.corpserver.dto.SyncCompanyDTO_Create;
import com.maoding.corp.module.corpserver.dto.SyncCompanyDTO_Select;
import com.maoding.corp.module.corpserver.dto.SyncCompanyDTO_Update;
import com.maoding.corp.module.corpserver.model.SyncCompanyDO;
import com.maoding.utils.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wuwq on 2017/2/10.
 */
@Service("syncCompanyServise")
public class SyncCompanyServiseImpl extends BaseService implements SyncCompanyServise {

    private static final Logger logger = LoggerFactory.getLogger(SyncCompanyServiseImpl.class);

    @Autowired
    private SyncCompanyDAO syncCompanyDao;

    @Autowired
    private RedissonClient redissonClient;

    //增加redis记录（读写锁）
    private void redisAdd(SyncCompanyDO sc) {
        RReadWriteLock lock = redissonClient.getReadWriteLock(RKey.LOCK_CORP_EP_C);
        RLock r = lock.writeLock();
        r.lock(5, TimeUnit.SECONDS);

        RSet<String> set = redissonClient.getSet(RKey.CORP_EP_C);
        String item = String.format("%s:%s", sc.getCorpEndpoint(), sc.getCompanyId());
        set.add(item);

        r.unlock();
    }

    //删除redis记录（读写锁）
    private void redisDelete(String corpEndpoint, String companyId) {
        RReadWriteLock lock = redissonClient.getReadWriteLock(RKey.LOCK_CORP_EP_C);
        RLock r = lock.writeLock();
        r.lock(5, TimeUnit.SECONDS);

        RSet<String> set = redissonClient.getSet(RKey.CORP_EP_C);
        String item = String.format("%s:%s", corpEndpoint, companyId);
        set.remove(item);

        r.unlock();
    }


    @Override
    public ApiResult create(SyncCompanyDTO_Create dto) {
        SyncCompanyDO entity = new SyncCompanyDO();
        BeanUtils.copyProperties(dto, entity);
        entity.initEntity();

        Example example = new Example(SyncCompanyDO.class, true);
        example.createCriteria()
                .andCondition("corp_endpoint = ", dto.getCorpEndpoint())
                .andCondition("company_id = ", dto.getCompanyId());
        int existCount = syncCompanyDao.selectCountByExample(example);
        if (existCount > 0)
            return ApiResult.failed("创建失败，重复记录", null);

        if (syncCompanyDao.insert(entity) > 0) {
            //增加redis记录（读写锁）
            //TODO 待考虑失败的情况
            redisAdd(entity);

            return ApiResult.success(null, entity);
        }

        return ApiResult.failed(null, null);
    }

    @Override
    public ApiResult update(SyncCompanyDTO_Update dto) {
        SyncCompanyDO entity = new SyncCompanyDO();
        BeanUtils.copyProperties(dto, entity);
        entity.resetUpdateDate();

        if (syncCompanyDao.updateByPrimaryKeySelective(entity) > 0)
            return ApiResult.success(null, null);

        return ApiResult.failed(null, null);
    }

    @Override
    public ApiResult delete(String corpEndpoint, String companyId) {
        Example example = new Example(SyncCompanyDO.class);
        example.createCriteria()
                .andCondition("corp_endpoint = ", corpEndpoint)
                .andCondition("company_id = ", companyId);
        if (syncCompanyDao.deleteByExample(example) > 0) {
            //删除redis记录（读写锁）
            //TODO 待考虑失败的情况
            redisDelete(corpEndpoint, companyId);
            return ApiResult.success(null, null);
        }
        return ApiResult.failed(null, null);
    }

    @Override
    public ApiResult delete(String id) {
        SyncCompanyDO sc = syncCompanyDao.selectByPrimaryKey(id);
        if (syncCompanyDao.deleteByPrimaryKey(id) > 0) {
            //删除redis记录（读写锁）
            //TODO 待考虑失败的情况
            redisDelete(sc.getCorpEndpoint(), sc.getCompanyId());
            return ApiResult.success(null, null);
        }
        return ApiResult.failed(null, null);
    }

    @Override
    public ApiResult select(String corpEndpoint) {
        List<SyncCompanyDTO_Select> dtos;
        if (StringUtils.isNotBlank(corpEndpoint))
            dtos = syncCompanyDao.selectSyncCompany(corpEndpoint);
        else
            dtos = syncCompanyDao.selectSyncCompany(null);

        return ApiResult.success(null, dtos);
    }


    /**
     * 同步组织到Redis
     */
    @Override
    public ApiResult syncToRedis() {
        List<SyncCompanyDTO_Select> dtos = syncCompanyDao.selectSyncCompany(null);

        List<String> vals = Lists.newArrayList();
        dtos.forEach(sc -> vals.add(sc.getCorpEndpoint() + ":" + sc.getCompanyId()));

        RReadWriteLock lock = redissonClient.getReadWriteLock(RKey.LOCK_CORP_EP_C);
        RLock r = lock.readLock();
        r.lock(5, TimeUnit.SECONDS);

        RSet<String> set = redissonClient.getSet(RKey.CORP_EP_C);
        set.clear();
        set.addAll(vals);

        r.unlock();

        return ApiResult.success(null, dtos);
    }
}
