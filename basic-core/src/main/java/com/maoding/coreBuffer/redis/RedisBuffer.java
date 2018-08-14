package com.maoding.coreBuffer.redis;

import com.maoding.coreBuffer.CoreBuffer;
import com.maoding.utils.BeanUtils;
import com.maoding.utils.ObjectUtils;
import com.maoding.utils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 深圳市卯丁技术有限公司
 * @author  : 张成亮
 * 日    期 : 2018/6/1 11:25
 * 描    述 :
 */
public class RedisBuffer implements CoreBuffer {
    private static final Logger log = LoggerFactory.getLogger(RedisBuffer.class);

    private static final String DEFAULT_REDIS_ADDRESS = "172.16.6.73:6679";
    private static final String DEFAULT_REDIS_PASSWORD = "123456";
    private static final String DEFAULT_LOCK_NAME = "RedisBuffer";
    private static final Integer DEFAULT_BUFFER_LOCK_SECONDS = 5;

    private RedissonClient lastClient = null;

    private String address;
    private String password;
    private String name;
    private Integer lockTime;

    public Integer getLockTime() {
        return ObjectUtils.isEmpty(lockTime) ? DEFAULT_BUFFER_LOCK_SECONDS : lockTime;
    }

    public void setLockTime(Integer lockTime) {
        this.lockTime = lockTime;
    }

    public String getName() {
        return StringUtils.isEmpty(name) ? DEFAULT_LOCK_NAME + ":" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return StringUtils.isEmpty(address) ? DEFAULT_REDIS_ADDRESS : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return StringUtils.isEmpty(password) ? DEFAULT_REDIS_PASSWORD : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private RedissonClient getClient() {
        if (lastClient == null) {
            Config config = new Config();
            SingleServerConfig ssc = config.useSingleServer();
            ssc.setAddress(getAddress());
            if (!StringUtils.isEmpty(getPassword())) {
                ssc.setPassword(getPassword());
            }
            lastClient = Redisson.create(config);
        }
        return lastClient;
    }


    @Override
    public <T> boolean replaceInList(@NotNull T element, long aliveTime) {
        RReadWriteLock lock = getClient().getReadWriteLock(DEFAULT_LOCK_NAME);
        RLock r = lock.writeLock();
        r.lock(getLockTime(), TimeUnit.SECONDS);

        RKeys rKeys = getClient().getKeys();
        Iterable<String> keys = rKeys.getKeysByPattern(getName() + "*");
        boolean found = false;
        for (String key : keys) {
            RList<T> rList = getClient().getList(key);
            for (T e : rList){
                if (StringUtils.isSame(BeanUtils.getId(e),BeanUtils.getId(element))){
                    rList.remove(e);
                    rList.add(element);
                    if (aliveTime > 0) {
                        rList.expireAt(getRedisTime() + aliveTime);
                    }
                    found = true;
                    break;
                }
            }
        }
        r.unlock();

        return found;
    }

    @Override
    public <T> void removeListInclude(@NotNull T element) {
        RReadWriteLock lock = getClient().getReadWriteLock(DEFAULT_LOCK_NAME);
        RLock r = lock.writeLock();
        r.lock(getLockTime(), TimeUnit.SECONDS);

        RKeys rKeys = getClient().getKeys();
        Iterable<String> keys = rKeys.getKeysByPattern(getName() + "*");
        List<String> keyList = new ArrayList<>();
        for (String key : keys) {
            RList<T> rList = getClient().getList(key);
            for (T e : rList){
                if (StringUtils.isSame(BeanUtils.getId(e),BeanUtils.getId(element))){
                    keyList.add(key);
                    break;
                }
            }
        }

        if (ObjectUtils.isNotEmpty(keyList)){
            for (String key : keyList) {
                rKeys.delete(key);
            }
        }

        r.unlock();
    }

    @Override
    public <T> void setList(@NotNull String key, @NotNull List<? extends T> list, long aliveTime) {
        RReadWriteLock lock = getClient().getReadWriteLock(DEFAULT_LOCK_NAME);
        RLock r = lock.writeLock();
        r.lock(getLockTime(), TimeUnit.SECONDS);

        RList<T> rList = getClient().getList(getName() + key);
        rList.clear();
        rList.addAll(list);
        if (aliveTime > 0) {
            rList.expireAt(getRedisTime() + aliveTime);
        }

        r.unlock();
    }

    @Override
    public <T> List<T> getList(@NotNull String key, long aliveTime) {
        RReadWriteLock lock = getClient().getReadWriteLock(DEFAULT_LOCK_NAME);
        RLock r = lock.writeLock();
        r.lock(getLockTime(), TimeUnit.SECONDS);

        RList<T> rList = getClient().getList(getName() + key);
        if (aliveTime > 0) {
            rList.expireAt(getRedisTime() + aliveTime);
        }
        List<T> list = new ArrayList<>();
        list.addAll(rList);

        r.unlock();

        return list;
    }

    private void removeList(@NotNull String key) {
        RReadWriteLock lock = getClient().getReadWriteLock(DEFAULT_LOCK_NAME);
        RLock r = lock.writeLock();
        r.lock(getLockTime(), TimeUnit.SECONDS);

        RKeys rKeys = getClient().getKeys();
        Iterable<String> keys = rKeys.getKeys();
        for (String k : keys) {
            log.info(k);
            if (StringUtils.isSame("{\"id\":\"1111\"}.SimpleNodeDTO.Alive",k) ||
                    StringUtils.isSame("RedisBuffer",k)
                    ){
                rKeys.delete(k);
            }
        }

        r.unlock();
    }

    private long getRedisTime() {
        Iterator<Node> nodeIterator = getClient().getNodesGroup().getNodes().iterator();
        return (nodeIterator.next().time() + 1) * 1000;
    }
}
