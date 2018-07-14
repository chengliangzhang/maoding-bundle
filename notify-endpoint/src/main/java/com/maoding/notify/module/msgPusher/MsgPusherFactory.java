package com.maoding.notify.module.msgPusher;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：MsgpusherFactory
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年8月28日-下午2:40:22
 */
public class MsgPusherFactory {
    private static MsgPusherFactory msgpusherFactory;
    /** 应用ID */
    private String appid;
    /** 推送所需密钥 */
    private String appkey;
    /** 调用推送接口令牌 */
    private String appToken;
    /** 接口API地址 */
    private String host;

    protected MsgPusherFactory() {
        super();
    }

    public static synchronized MsgPusherFactory getInstance() {
        if (msgpusherFactory == null)
            msgpusherFactory = new MsgPusherFactory();
        return msgpusherFactory;
    }

    public MsgPusher defaultMsgpusher() {
        return createMsgpusher(MsgPusherType.IGTPush);
    }

    public MsgPusher createMsgpusher(MsgPusherType msgpusherType) {
        if (msgpusherType == MsgPusherType.IGTPush) {
            GTPusher pusher;
            if (host == null || "".equals(host)) {
                pusher = new GTPusher(appkey, appToken);
            } else {
                pusher = new GTPusher(appkey, appToken, host);
            }
            pusher.setAppid(appid);
            return pusher;
        } else {
            return null;
        }
    }

    /**
     * 获取：appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置：appid
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * 获取：推送所需密钥
     */
    public String getAppkey() {
        return appkey;
    }

    /**
     * 设置：推送所需密钥
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    /**
     * 获取：调用推送接口令牌
     */
    public String getAppToken() {
        return appToken;
    }

    /**
     * 设置：调用推送接口令牌
     */
    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    /**
     * 获取：接口API地址
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置：接口API地址
     */
    public void setHost(String host) {
        this.host = host;
    }

}
