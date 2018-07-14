package com.maoding.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Wuwq on 2017/07/13.
 */
@Configuration
@ConfigurationProperties(prefix = "fastdfs")
public class FastdfsConfig {
    private String webServerUrl;

    public String getWebServerUrl() {
        return webServerUrl;
    }

    public void setWebServerUrl(String webServerUrl) {
        this.webServerUrl = webServerUrl;
    }
}
