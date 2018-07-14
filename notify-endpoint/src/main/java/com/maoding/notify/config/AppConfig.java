package com.maoding.notify.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Wuwq on 2016/10/10.
 */
@Configuration
public class AppConfig {

    @Value("${app.http.host}")
    private String httpHost;
    @Value("${app.http.port}")
    private int httpPort;
    @Value("${app.sockJs.host}")
    private String sockJsHost;
    @Value("${app.sockJs.port}")
    private int sockJsPort;
    @Value("${app.sockJs.endPoint}")
    private String sockJsEndPoint;
    @Value("${app.clientSubUrl_TopicAll}")
    private String clientSubUrl_TopicAll;
    @Value("${app.clientSubUrl_TopicSpec}")
    private String clientSubUrl_TopicSpec;

    public String getHttpHost() {

        return httpHost;
    }

    public void setHttpHost(String httpHost) {
        this.httpHost = httpHost;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getSockJsHost() {
        return sockJsHost;
    }

    public void setSockJsHost(String sockJsHost) {
        this.sockJsHost = sockJsHost;
    }

    public int getSockJsPort() {
        return sockJsPort;
    }

    public void setSockJsPort(int sockJsPort) {
        this.sockJsPort = sockJsPort;
    }

    public String getSockJsEndPoint() {
        return sockJsEndPoint;
    }

    public void setSockJsEndPoint(String sockJsEndPoint) {
        this.sockJsEndPoint = sockJsEndPoint;
    }

    public String getClientSubUrl_TopicAll() {
        return clientSubUrl_TopicAll;
    }

    public void setClientSubUrl_TopicAll(String clientSubUrl_TopicAll) {
        this.clientSubUrl_TopicAll = clientSubUrl_TopicAll;
    }

    public String getClientSubUrl_TopicSpec() {
        return clientSubUrl_TopicSpec;
    }

    public void setClientSubUrl_TopicSpec(String clientSubUrl_TopicSpec) {
        this.clientSubUrl_TopicSpec = clientSubUrl_TopicSpec;
    }
}
