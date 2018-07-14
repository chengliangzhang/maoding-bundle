package com.maoding.aliyunoss.domain;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.maoding.aliyunoss.AliyunossClientConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by sandy on 2017/10/20.
 */
@Component
@ConfigurationProperties(prefix = AliyunossClientConstants.ROOT_CONFIG_PREFIX_OSS)
public class OssConnectionManager{

    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret ;

    private long idleConnectionTime;

    private ClientConfiguration conf = new ClientConfiguration();

    private static OSSClient client = null;

    public static synchronized OSSClient getInstance() {
        if (client == null) {
            client = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        }
        return client;
    }

    /**
     * 初始化方法
     */
    @PostConstruct
    public void initOSSClient() {
        conf.setIdleConnectionTime(idleConnectionTime);
            conf.setMaxConnections(10); // 设置HTTP最大连接数为10
            conf.setConnectionTimeout(2000); // 设置TCP连接超时为5000毫秒
            conf.setMaxErrorRetry(3); // 设置最大的重试次数为3
           conf.setSocketTimeout(5000); // 设置Socket传输数据超时的时间为2000毫秒
        client = new OSSClient(endpoint,accessKeyId,accessKeySecret);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public OSSClient getClient() {
        return client;
    }

    public void setClient(OSSClient client) {
        this.client = client;
    }

    public long getIdleConnectionTime() {
        return idleConnectionTime;
    }

    public void setIdleConnectionTime(long idleConnectionTime) {
        this.idleConnectionTime = idleConnectionTime;
    }
}
