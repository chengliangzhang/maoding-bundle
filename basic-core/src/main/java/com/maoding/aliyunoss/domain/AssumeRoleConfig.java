package com.maoding.aliyunoss.domain;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by sandy on 2017/10/25.
 */
@Configuration
@ConfigurationProperties(prefix = "aliyunsts")
public class AssumeRoleConfig {

    // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
    // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
    // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
    private static String accessKeyId ;
    private static String accessKeySecret;
    // AssumeRole API 请求参数: RoleArn, RoleSessionName, Policy, and DurationSeconds
    // RoleArn 需要在 RAM 控制台上获取
    private static String roleArn;
    // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
    // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
    // 具体规则请参考API文档中的格式要求
    private static String roleSessionName;

    private static long tokenExpireTime;
    // 定制你的policy
    private static String policy =   "{ \"Statement\": [" +
            "{\"Action\": \"sts:AssumeRole\", \"Effect\": \"Allow\",\"Resource\": \"*\" }," +
            "{\"Action\": \"oss:*\", \"Effect\": \"Allow\",\"Resource\": \"*\" }" +
            "]" +
            ",\"Version\": \"1\"}";
    // 此处必须为 HTTPS

    private static ProtocolType protocolType = ProtocolType.HTTPS;

    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    private static String REGION_CN_HANGZHOU = "cn-shenzhen";
    // 当前 STS API 版本
    private static String STS_API_VERSION = "2015-04-01";



    public static AssumeRoleResponse assumeRole() throws com.aliyuncs.exceptions.ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(tokenExpireTime);
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response;
        } catch (com.aliyuncs.exceptions.ClientException e) {
            throw e;
        }
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getRoleSessionName() {
        return roleSessionName;
    }

    public void setRoleSessionName(String roleSessionName) {
        this.roleSessionName = roleSessionName;
    }

    public static long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public static void setTokenExpireTime(long tokenExpireTime) {
        AssumeRoleConfig.tokenExpireTime = tokenExpireTime;
    }
}
