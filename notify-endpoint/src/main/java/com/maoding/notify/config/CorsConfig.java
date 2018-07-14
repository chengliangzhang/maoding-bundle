package com.maoding.notify.config;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.handler.CorsHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Wuwq on 2016/10/12.
 */
@Configuration
public class CorsConfig {

    @Value("${cors.allowedOriginPattern}")
    private String allowedOriginPattern;

    public String getAllowedOriginPattern() {
        return allowedOriginPattern;
    }

    public void setAllowedOriginPattern(String allowedOriginPattern) {
        this.allowedOriginPattern = allowedOriginPattern;
    }

    @Bean
    public CorsHandler getCorsHandler() {
        return CorsHandler.create(allowedOriginPattern)
                .allowCredentials(true)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.DELETE)
                .allowedHeader("Access-Control-Request-Method")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type");
    }
}
