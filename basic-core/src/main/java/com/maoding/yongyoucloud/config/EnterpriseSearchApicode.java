package com.maoding.yongyoucloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "enterprisesearch")
public class EnterpriseSearchApicode {

    public static String apicode  ;//"7c250712d6f44926904a46e4d0e6b800";

    public static Map<String,String> header = new HashMap<>();
    public String getApicode() {
        return apicode;
    }

    public void setApicode(String apicode) {
        EnterpriseSearchApicode.apicode = apicode;
        header.put("authoration",EnterpriseSearchApicode.apicode);
        header.put("apicode",EnterpriseSearchApicode.apicode);
    }
}
