package com.maoding.im.config;

import com.maoding.im.easemob.comm.ApiResultHandler;
import io.swagger.client.api.*;
import io.swagger.client.model.Token;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Wuwq on 2016/10/13.
 */
@Configuration
@ConfigurationProperties(prefix = "easemob")
public class EasemobConfig {

    public static String ORG_NAME;
    public static String APP_NAME;
    public static String GRANT_TYPE;
    public static String CLIENT_ID;
    public static String CLIENT_SECRET;

    public void setOrgName(String orgName) {
        ORG_NAME = orgName;
    }

    public void setAppName(String appName) {
        APP_NAME = appName;
    }

    public void setGrantType(String grantType) {
        GRANT_TYPE = grantType;
    }

    public void setClientId(String clientId) {
        CLIENT_ID = clientId;
    }

    public void setClientSecret(String clientSecret) {
        CLIENT_SECRET = clientSecret;
    }

    @Bean
    public Token getToken() {
        return new Token().
                clientId(CLIENT_ID).
                grantType(GRANT_TYPE).
                clientSecret(CLIENT_SECRET);
    }

    @Bean
    public AuthenticationApi getAuthenticationApi() {
        return new AuthenticationApi();
    }

    @Bean
    public GroupsApi getGrousApi() {
        return new GroupsApi();
    }

    @Bean
    public ChatHistoryApi getChatHistoryApi() {
        return new ChatHistoryApi();
    }

    @Bean
    public ChatRoomsApi getChatRoomsApi() {
        return new ChatRoomsApi();
    }

    @Bean
    public UploadAndDownloadFilesApi getUploadAndDownloadFilesApi() {
        return new UploadAndDownloadFilesApi();
    }

    @Bean
    public UsersApi getUsersApi() {
        return new UsersApi();
    }

    @Bean
    public MessagesApi getMessagesApi() {
        return new MessagesApi();
    }

    @Bean
    public ApiResultHandler getApiResultHandler() {
        return new ApiResultHandler();
    }
}
