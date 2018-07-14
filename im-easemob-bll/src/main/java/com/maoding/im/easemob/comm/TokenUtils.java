package com.maoding.im.easemob.comm;

import com.google.gson.Gson;
import com.maoding.im.config.EasemobConfig;
import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by easemob on 2017/3/14.
 */
@Configuration
public class TokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    private static String ACCESS_TOKEN;
    private static Double EXPIREDAT = -1D;


    private static Token BODY;
    private static AuthenticationApi AUTHENTICATION_API;

    public static void initTokenByProp() {
        String resp = null;
        try {
            resp = AUTHENTICATION_API.orgNameAppNameTokenPost(EasemobConfig.ORG_NAME, EasemobConfig.APP_NAME, BODY);
        } catch (ApiException e) {
            logger.error(e.getMessage());
        }
        Gson gson = new Gson();
        Map map = gson.fromJson(resp, Map.class);
        ACCESS_TOKEN = " Bearer " + map.get("access_token");
        EXPIREDAT = System.currentTimeMillis() + (Double) map.get("expires_in");
    }

    public static String getAccessToken() {
        if (ACCESS_TOKEN == null || isExpired())
            initTokenByProp();
        return ACCESS_TOKEN;
    }

    private static Boolean isExpired() {
        return System.currentTimeMillis() > EXPIREDAT;
    }

    @Autowired
    public void SetAuthenticationApi(AuthenticationApi authenticationApi) {
        AUTHENTICATION_API = authenticationApi;
    }

    @Autowired
    public void setBody(Token token) {
        BODY = token;
    }

}

