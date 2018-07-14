package com.maoding.notify.module.msgPusher;

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.maoding.notify.module.message.dto.SendMessageDataDTO;
import com.maoding.notify.utils.JsonUtils;
import com.maoding.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：GTPusher
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年8月28日-下午2:53:17
 */
public class GTPusher implements MsgPusher {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private IGtPush iGTPush;
    private String appid;
    private String appkey;

    protected GTPusher(String appkey, String appToken) {
        this.appkey = appkey;
        this.iGTPush = new IGtPush(appkey, appToken);
    }

    protected GTPusher(String appkey, String appToken, String host) {
        this.appkey = appkey;
        this.iGTPush = new IGtPush(host, appkey, appToken);
    }

    /**
     * 获取：iGTPush
     */
    public IGtPush getiGTPush() {
        return iGTPush;
    }

    /**
     * 设置：iGTPush
     */
    public void setiGTPush(IGtPush iGTPush) {
        this.iGTPush = iGTPush;
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
     * 获取：appkey
     */
    public String getAppkey() {
        return appkey;
    }

    /**
     * 设置：appkey
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    @Override
    public void pushSystemMsg(String title, String content) {
        NotificationTemplate notification = new NotificationTemplate();
        notification.setAppId(appid);
        notification.setAppkey(appkey);
        notification.setTitle(title);
        notification.setText(content);
        notification.setLogo("icon.png");
        notification.setLogoUrl("");
        notification.setIsRing(true);
        notification.setIsVibrate(true);
        notification.setIsClearable(true);
        notification.setTransmissionType(1);
        notification.setTransmissionContent("text");

        AppMessage msg = new AppMessage();
        msg.setData(notification);
        msg.setOffline(true);
        msg.setOfflineExpireTime(24 * 1000 * 3600);
        List<String> appIdList = new ArrayList<String>();
        appIdList.add(appid);
        msg.setAppIdList(appIdList);

        IPushResult result = iGTPush.pushMessageToApp(msg, "设计同道后台系统消息");
        log.debug("设计同道后台系统消息::{}", result.getResponse());
    }

    @Override
    public boolean bindMultyCID(String userid, Collection<? extends Object> cids) {
        List<Target> cidsL = new ArrayList<Target>();
        for (Object cid : cids) {
            Target t = new Target();
            t.setAlias(userid);
            t.setClientId((String) cid);
            cidsL.add(t);
        }
        IAliasResult rs = iGTPush.bindAlias(appid, cidsL);
        if (!rs.getResult()) {
            log.debug(rs.getErrorMsg());
        }
        return rs.getResult();
    }

    @Override
    public boolean bindMultyCID(String userid, String... cids) {
        List<Target> cidsL = new ArrayList<Target>();
        for (String cid : cids) {
            Target t = new Target();
            t.setAlias(userid);
            t.setClientId(cid);
            cidsL.add(t);
        }
        IAliasResult rs = iGTPush.bindAlias(appid, cidsL);
        if (!rs.getResult()) {
            log.debug(rs.getErrorMsg());
        }
        return rs.getResult();
    }

    @Override
    public List<String> findCIDbyUserid(String userid) {
        IAliasResult rs = iGTPush.queryClientId(appid, userid);
        return rs.getClientIdList();
    }

    @Override
    public String findUseridByCID(String cid) {
        IAliasResult rs = iGTPush.queryAlias(appid, cid);
        return rs.getAlias();
    }

    @Override
    public boolean unbindCID(String userid, String cid) {
        IAliasResult rs = iGTPush.unBindAlias(appid, userid, cid);
        return rs.getResult();
    }

    @Override
    public boolean unbindAllCID(String userid) {
        IAliasResult rs = iGTPush.unBindAliasAll(appid, userid);
        return rs.getResult();
    }

    @Override
    public void pushMsgToUser(String userid, String title, String content) {
        TransmissionTemplate transmission = new TransmissionTemplate();
        transmission.setAppId(appid);
        transmission.setAppkey(appkey);
        //transmission.setTransmissionType(1);
        //{"title":"测试的标题","content":"测试的内容"}
        transmission.setTransmissionContent("{'title':'" + title + "','content':'" + content + "'}");
        //transmission.setTransmissionContent( "{\"title\":"\"++title+\""+","+"\"content\":"+"\"+content+\"}");
        transmission.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        payload.setBadge(1);  //将应用icon上显示的数字设为1
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，如10，效果和setBadge一样，
        //应用icon上显示指定数字。不能和setBadge同时使用
        //setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");
        //简单模式APNPayload.SimpleMsg
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(content);
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("收到一条系统消息");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // IOS8.2以上版本支持
        alertMsg.setTitle(title);
        alertMsg.setTitleLocKey("卯丁");
        alertMsg.addTitleLocArg("TitleLocArg");
        payload.setAlertMsg(alertMsg);
        //字典模式使用下者
        //payload.setAlertMsg(getDictionaryAlertMsg());
        transmission.setAPNInfo(payload);

        SingleMessage message = new SingleMessage();

        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(transmission);
        message.setPushNetWorkType(0); //可选。判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。

        Target target = new Target();
        target.setAppId(appid);
        target.setAlias(userid);
        IPushResult result = iGTPush.pushMessageToSingle(message, target);
        log.debug("卯丁后台系统消息::{}", result.getResponse());
    }

    /**
     * 方法描述：给指定用户推送消息
     * 作        者：Chenxj
     * 日        期：2015年9月9日-下午2:40:45
     * @param userList
     * @param title
     * @param content
     */
    @Override
    public void pushMsgToUserList(List<String> userList, String title, String content) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appid);
        template.setAppkey(appkey);
        // 设置通知栏标题与内容
        template.setTitle(title);
        template.setText(content);
        // 配置通知栏图标
        //template.setLogo("icon.png");
        // 配置通知栏网络图标
        //template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent("{'title':'" + title + "','content':'" + content + "'}");

//		TransmissionTemplate  transmission=new TransmissionTemplate();
//		transmission.setAppId(appid);
//		transmission.setAppkey(appkey);
//		//transmission.setTransmissionType(1);
//		//{"title":"测试的标题","content":"测试的内容"}
//		transmission.setTransmissionContent( "{'title':'"+title+"','content':'"+content+"'}");
//		//transmission.setTransmissionContent( "{\"title\":"\"++title+\""+","+"\"content\":"+"\"+content+\"}");
//		transmission.setTransmissionType(2);
//		APNPayload payload = new APNPayload();
//		payload.setBadge(1);  //将应用icon上显示的数字设为1
//		//在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，如10，效果和setBadge一样，
//		//应用icon上显示指定数字。不能和setBadge同时使用
//		//setAutoBadge("+1");
//		payload.setContentAvailable(1);
//		payload.setSound("default");
//		payload.setCategory("$由客户端定义");
//		//简单模式APNPayload.SimpleMsg
//		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
//		alertMsg.setBody(content);
//		alertMsg.setActionLocKey("ActionLockey");
//		alertMsg.setLocKey("收到一条系统消息");
//		alertMsg.addLocArg("loc-args");
//		alertMsg.setLaunchImage("launch-image");
//		// IOS8.2以上版本支持
//		alertMsg.setTitle(title);
//		alertMsg.setTitleLocKey("卯丁");
//		alertMsg.addTitleLocArg("TitleLocArg");
//		payload.setAlertMsg(alertMsg);
//		//字典模式使用下者
//		//payload.setAlertMsg(getDictionaryAlertMsg());
//		transmission.setAPNInfo(payload);


        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        // 配置返回每个别名及其对应cid的用户状态，可选
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();
        for (String userId : userList) {
            Target target1 = new Target();
            target1.setAppId(appid);
            target1.setAlias(userId);
            targets.add(target1);
        }

        // taskId用于在推送时去查找对应的message
        String taskId = iGTPush.getContentId(message);
        IPushResult ret = iGTPush.pushMessageToList(taskId, targets);
        System.out.println(ret.getResponse().toString());
    }

    /**
     * 方法描述：给指定用户推送消息
     * 作        者：Chenxj
     * 日        期：2015年9月9日-下午2:40:45
     */
    @Override
    public void pushRoleMsgToUserList(SendMessageDataDTO msg) {

        TransmissionTemplate transmission = new TransmissionTemplate();
        transmission.setAppId(appid);
        transmission.setAppkey(appkey);
        //transmission.setTransmissionType(1);
        //{"title":"测试的标题","content":"测试的内容"}
        Map<String,Object> map = msg.getOtherContent();
        map.put("title",msg.getTitle());
        map.put("content",msg.getContent());
        map.put("type",msg.getMessageType());
        map.put("sendTime", new Date());
        try{
            transmission.setTransmissionContent(JsonUtils.obj2json(map));
        }catch (Exception e){
            log.error("GTPusher--》pushRoleMsgToUserList 发生异常");
        }
        //transmission.setTransmissionContent( "{\"title\":"\"++title+\""+","+"\"content\":"+"\"+content+\"}");
        transmission.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //payload.setBadge(1);  //将应用icon上显示的数字设为1
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，如10，效果和setBadge一样，
        //应用icon上显示指定数字。不能和setBadge同时使用
        //setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");
        //简单模式APNPayload.SimpleMsg
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(msg.getContent());
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("收到一条系统消息");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // IOS8.2以上版本支持
        alertMsg.setTitle(msg.getTitle());
        alertMsg.setTitleLocKey("卯丁");
        alertMsg.addTitleLocArg("TitleLocArg");
        payload.setAlertMsg(alertMsg);
        //字典模式使用下者
        //payload.setAlertMsg(getDictionaryAlertMsg());
        transmission.setAPNInfo(payload);

        // 配置返回每个别名及其对应cid的用户状态，可选
        ListMessage message = new ListMessage();
        message.setData(transmission);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(48 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();
        for (String userId : msg.getReceiverList()) {
            Target target1 = new Target();
            target1.setAppId(appid);
            target1.setAlias(userId);
            targets.add(target1);
        }

        // taskId用于在推送时去查找对应的message
        String taskId = iGTPush.getContentId(message);
        IPushResult ret = iGTPush.pushMessageToList(taskId, targets);
        System.out.println(ret.getResponse().toString());
    }
}
