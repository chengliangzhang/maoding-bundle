package com.maoding.corp.module.corpclient.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.maoding.constDefine.corp.*;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.corp.config.CorpClientConfig;
import com.maoding.corp.module.corpclient.dao.SyncTaskDAO;
import com.maoding.corp.module.corpclient.dto.PushResult;
import com.maoding.corp.module.corpclient.model.SyncTaskDO;
import com.maoding.utils.GsonUtils;
import com.maoding.utils.JsonUtils;
import com.maoding.utils.OkHttpUtils;
import com.maoding.utils.StringUtils;
import okhttp3.Response;
import org.redisson.api.RLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wuwq on 2017/05/24.
 */
@Service("syncService")
public class SyncServiceImpl extends BaseService implements SyncService {

    private static final Logger log = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Autowired
    private SyncTaskDAO syncTaskDao;

    @Autowired
    private RedissonClient redissonClient;

    private Set<String> pullChangesFromRedis(String lockKey, String key) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(5, TimeUnit.SECONDS);

        RSet<String> set = redissonClient.getSet(key);
        Set<String> changes = set.readAll();

        lock.unlock();

        return changes;
    }

    private void removeChangesFromRedis(String lockKey, String key, Set<String> changes) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(5, TimeUnit.SECONDS);

        RSet<String> set = redissonClient.getSet(key);
        set.removeAll(changes);

        lock.unlock();
    }

    /**
     * 根据端点拉取组织变更（CU、CD）
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void pullChanges_C(String endpoint) {
        String lockKey = String.format(RKey.LOCK_CORP_EP_SYNC_C_PATTERN, endpoint).toUpperCase();
        String key = String.format(RKey.CORP_EP_SYNC_C_PATTERN, endpoint).toUpperCase();
        Set<String> changes = pullChangesFromRedis(lockKey, key);

        //同步时间点附加10分钟误差，防止服务器端时间不同步
        LocalDateTime syncPoint = LocalDateTime.now().plusMinutes(10);
        ArrayList<SyncTaskDO> tasks = Lists.newArrayList();

        if (changes.size() > 0) {
            log.info("端点 {} 发现 {} 个变更（CU）", endpoint, changes.size());

            for (String c : changes) {
                String[] splits = StringUtils.split(c, ":");

                SyncTaskDO t = new SyncTaskDO();
                t.initEntity();
                t.setCorpEndpoint(endpoint);
                t.setCompanyId(splits[1]);
                t.setSyncPoint(syncPoint);
                t.setSyncCmd(splits[0].equalsIgnoreCase(SyncCmd.CU)?SyncCmd.CU:SyncCmd.CD);
                t.setRetryTimes(0);
                t.setUpVersion(0L);

                //优先级最高
                t.setSyncPriority(SyncPriority.L1);
                t.setSyncStatus(SyncStatus.WaitSync);
                t.setTaskStatus(TaskStatus.WaitRuning);

                tasks.add(t);
            }

            log.info("正在将端点 {} 的CU变更写入本地数据库", endpoint);
            if (syncTaskDao.BatchInsert(tasks) == tasks.size()) {
                log.info("正在从 Redis 移除端点 {} 的已拉取变更", endpoint);
                removeChangesFromRedis(lockKey, key, changes);
            }
        }
    }

    /**
     * 根据端点和项目ID拉取变更
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void pullChanges_P_ID(String endpoint, String projectId) {
        String lockKey = String.format(RKey.LOCK_CORP_EP_SYNC_P_ID_PATTERN, endpoint, projectId).toUpperCase();
        String key = String.format(RKey.CORP_EP_SYNC_P_ID_PATTERN, endpoint, projectId).toUpperCase();
        Set<String> changes = pullChangesFromRedis(lockKey, key);

        //同步时间点附加10分钟误差，防止服务器端时间不同步
        LocalDateTime syncPoint = LocalDateTime.now().plusMinutes(10);
        ArrayList<SyncTaskDO> tasks = Lists.newArrayList();

        if (changes.size() == 0) {
            String lockKey2 = String.format(RKey.LOCK_CORP_EP_SYNC_P_PATTERN, endpoint).toUpperCase();
            String key2 = String.format(RKey.CORP_EP_SYNC_P_PATTERN, endpoint).toUpperCase();
            Set<String> sets = Sets.newHashSet();
            sets.add(projectId);
            removeChangesFromRedis(lockKey2, key2, sets);
            return;
        }

        log.info("端点 {} 项目 {} 发现 {} 个变更", endpoint, projectId, changes.size());

        //如果PALL可以忽略后面的
        if (changes.stream().anyMatch(c -> StringUtils.startsWithIgnoreCase(c, SyncCmd.PALL))) {
            SyncTaskDO t = new SyncTaskDO();
            t.initEntity();
            t.setCorpEndpoint(endpoint);
            t.setProjectId(projectId);
            t.setSyncPoint(syncPoint);
            t.setRetryTimes(0);
            t.setUpVersion(0L);
            t.setSyncStatus(SyncStatus.WaitSync);
            t.setTaskStatus(TaskStatus.WaitRuning);
            t.setSyncCmd(SyncCmd.PALL);
            t.setSyncPriority(SyncPriority.L2);
            tasks.add(t);
        } else {
            for (String c : changes) {
                String[] splits = StringUtils.split(c, ":");

                SyncTaskDO t = new SyncTaskDO();
                t.initEntity();
                t.setCorpEndpoint(endpoint);
                t.setProjectId(projectId);
                t.setSyncPoint(syncPoint);
                t.setRetryTimes(0);
                t.setUpVersion(0L);
                t.setSyncStatus(SyncStatus.WaitSync);
                t.setTaskStatus(TaskStatus.WaitRuning);

                if (StringUtils.startsWithIgnoreCase(c, SyncCmd.PU)) {
                    t.setSyncCmd(SyncCmd.PU);
                    t.setSyncPriority(SyncPriority.L2);
                } else if (StringUtils.startsWithIgnoreCase(c, SyncCmd.PT)) {
                    t.setSyncCmd(SyncCmd.PT);
                    t.setProjectPhaseId(splits[1]);
                    t.setSyncPriority(SyncPriority.L3);
                }
                tasks.add(t);
            }
        }

        log.info("正在将 端点 {} 项目 {} 的 {} 个变更写入数据库", endpoint, projectId, tasks.size());
        if (syncTaskDao.BatchInsert(tasks) == tasks.size()) {
            log.info("正在从 Redis 移除 端点 {} 项目 {} 的已拉取变更", endpoint, projectId);
            removeChangesFromRedis(lockKey, key, changes);
        }
    }

    /**
     * 拉取变更
     */
    @Override
    public void pullChanges(String endpoint) throws Exception {
        if (StringUtils.isBlank(endpoint))
            throw new RuntimeException("拉取变更失败，corpEndPoint为空");

        SyncService srv = (SyncService) AopContextCurrentProxy();

        //根据端点拉取组织变更
        srv.pullChanges_C(endpoint);

        //根据端点拉取变更的项目ID
        String lockKey = String.format(RKey.LOCK_CORP_EP_SYNC_P_PATTERN, endpoint).toUpperCase();
        String key = String.format(RKey.CORP_EP_SYNC_P_PATTERN, endpoint).toUpperCase();
        Set<String> changes = pullChangesFromRedis(lockKey, key);

        if (changes.size() > 0) {
            log.info("端点 {} 发现 {} 个项目", endpoint, changes.size());

            for (String projectId : changes) {
                try {
                    //根据端点和项目ID拉取变更
                    srv.pullChanges_P_ID(endpoint, projectId);
                } catch (Exception e) {
                    log.error(String.format("端点 {} 项目 {} 拉取变更发生异常", endpoint, projectId), e);
                }
            }
        }
    }

    /**
     * 从业务系统拉取数据（GET）
     */
    public ApiResult pullFromCorpServer(String url) {
        log.info("pullFromCorpServer {}", CorpClientConfig.CorpServer + url);


        Response res = null;
        try {
            res = OkHttpUtils.get(CorpClientConfig.CorpServer + url);
        } catch (IOException e) {
            throw new RuntimeException("请求CorpServer失败");
        }

        if (res.isSuccessful()) {
            try {
                return JsonUtils.json2pojo(res.body().string(), ApiResult.class);
            } catch (Exception e) {
                log.error("pullFromCorpServer 解析返回结果失败", e);
            }
        }
        try {
            return JsonUtils.json2pojo(res.body().string(), ApiResult.class);
        } catch (Exception e) {
            return ApiResult.failed(res.message(), null);
        }
    }

    /**
     * 从业务系统拉取数据（POST）
     */
    public ApiResult pullFromCorpServer(Map<String, Object> param, String url) {

        Response res = null;
        try {
            if (log.isDebugEnabled())
                log.debug("pullFromCorpServer {} {}", CorpClientConfig.CorpServer + url, JsonUtils.obj2json(param));
            else
                log.info("pullFromCorpServer {}", CorpClientConfig.CorpServer + url);
            res = OkHttpUtils.postJson(CorpClientConfig.CorpServer + url, param);
        } catch (IOException e) {
            throw new RuntimeException("请求CorpServer失败");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (res.isSuccessful()) {
            try {
                return JsonUtils.json2pojo(res.body().string(), ApiResult.class);
            } catch (Exception e) {
                log.error("pullFromCorpServer 解析返回结果失败", e);
            }
        }

        try {
            return JsonUtils.json2pojo(res.body().string(), ApiResult.class);
        } catch (Exception e) {
            return ApiResult.failed(res.message(), null);
        }
    }


    /**
     * 推送到对方协同服务端
     */
    public PushResult postToSOWServer(Object param, String url) throws Exception {
        if (log.isDebugEnabled())
            log.debug("postToSOWServer {} {}", CorpClientConfig.SowServer + url, JsonUtils.obj2json(param));
        else
            log.info("postToSOWServer {}", CorpClientConfig.SowServer + url);
        Response res = OkHttpUtils.postJson(CorpClientConfig.SowServer + url, param);
        if (res.isSuccessful()) {
            try {
                PushResult pushResult = GsonUtils.fromJson(res.body().string(), PushResult.class);
                return pushResult;
            } catch (Exception e) {
                log.error("postToSOWServer 解析返回结果失败", e);
            }
        }
        return PushResult.failed();
    }
}
