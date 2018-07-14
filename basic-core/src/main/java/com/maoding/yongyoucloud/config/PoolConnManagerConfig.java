package com.maoding.yongyoucloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "yongyou.poolconfig") //此處需要修改，導致程序啟動不了
public class PoolConnManagerConfig {
//
//    ##设置连接持最大连接数
//#maxTotal=200
//            ##设置最大并发访问个数
//#defaultMaxPerRoute=20
//            ##设置读取数据的毫秒级超时时间
//#soTimeout=10000
//            ##设置请求建立连接的毫秒级超时时间
//#connectionRequestTimeout=3000
//            ##设置连接建立时的毫秒级超时时间
//#connectTimeout=3000
//            ##设置套接字的毫秒级超时时间
//#socketTimeout=10000

    public static int maxTotal = 200 ;

     public static int defaultMaxPerRoute = 20;

     public static int soTimeout = 10000;

     public static int connectionRequestTimeout = 3000 ;

     public static int connectTimeout = 3000;

     public static int socketTimeout = 10000;

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
