package com.maoding.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

/**
 * Created by Wuwq on 2016/12/18.
 */
@Configuration
@ConfigurationProperties(prefix = "upload.multipart")
public class MultipartConfig {
    //文件上传时允许写到内存中的最大值,以字节为单位计算（默认10KB）
    public static int MaxInMemorySize = 10 * 1024;
    //最大上传文件大小,以字节为单位计算（默认20MB）
    public static int MaxUploadSize = 20 * 1024 * 1024;
    //分片大小,以字节为单位计算（默认5MB）
    public static int ChunkSize = 5 * 1024 * 1024;

    public void setMaxInMemorySize(int maxInMemorySize) {
        MaxInMemorySize = maxInMemorySize;
    }

    public void setMaxUploadSize(int maxUploadSize) {
        MaxUploadSize = maxUploadSize;
    }

    public void setChunkSize(int chunkSize) {
        ChunkSize = chunkSize;
    }

    @Bean
    @Order(0)
    public MultipartFilter multipartFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        return multipartFilter;
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以便在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(MaxInMemorySize);
        resolver.setMaxUploadSize(MaxUploadSize);
        return resolver;
    }
}
