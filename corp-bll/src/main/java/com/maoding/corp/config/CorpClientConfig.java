package com.maoding.corp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Wuwq on 2017/05/23.
 */
@Configuration
@ConfigurationProperties(prefix = "corpClient")
public class CorpClientConfig {

    /**
     * 卯丁协同客户端ID
     */
    public static String EndPoint;
    /**
     * 卯丁协同服务器
     */
    public static String CorpServer;
    /**
     * 第三方协同服务器
     */
    public static String SowServer;


    public void setEndPoint(String endPoint) {
        EndPoint = endPoint;
    }

    public void setCorpServer(String corpServer) {
        CorpServer = corpServer;
    }

    public void setSowServer(String SowServer) {
        CorpClientConfig.SowServer = SowServer;
    }
}
