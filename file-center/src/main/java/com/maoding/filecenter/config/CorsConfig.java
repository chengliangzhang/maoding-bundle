package com.maoding.filecenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConfigurationProperties(prefix = "cors")
public class CorsConfig extends WebMvcConfigurerAdapter {
    private String[] allowedOrgins;

    public String[] getAllowedOrgins() {
        return allowedOrgins;
    }

    public void setAllowedOrgins(String[] allowedOrgins) {
        this.allowedOrgins = allowedOrgins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/fileCenter/**").allowedOrigins(allowedOrgins).allowedMethods("POST");
    }
}
