package com.maoding.fastdfsClient.conn;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.stereotype.Component;

/**
 * 连接池配置
 *
 * @author tobato
 */
@Component
public class ConnectionPoolConfig extends GenericKeyedObjectPoolConfig {

    public ConnectionPoolConfig() {

        //卯丁旧配置
 /*       setMaxTotal(500); //设置线程池中的总最大数
        setMaxTotalPerKey(100);
        setMinIdlePerKey(0);
        setBlockWhenExhausted(true);//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        setMaxWaitMillis(20000);//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        setTestOnBorrow(false);//在获取连接的时候检查有效性, 默认false
        setTestOnReturn(false);
        setTestWhileIdle(false); //在空闲时检查有效性, 默认false
        setTimeBetweenEvictionRunsMillis(-1);//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        setMinEvictableIdleTimeMillis(1800000);// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        setSoftMinEvictableIdleTimeMillis(-1);//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
        setNumTestsPerEvictionRun(3);//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3*/


        setMaxTotal(50); // 从池中借出的对象的最大数目50
        setTestWhileIdle(true);
        setBlockWhenExhausted(true);
        setMaxWaitMillis(100);
        setMinEvictableIdleTimeMillis(180000); // 视休眠时间超过了180秒的对象为过期
        setTimeBetweenEvictionRunsMillis(60000); // 每过60秒进行一次后台对象清理的行动
        setNumTestsPerEvictionRun(-1);
    }

}