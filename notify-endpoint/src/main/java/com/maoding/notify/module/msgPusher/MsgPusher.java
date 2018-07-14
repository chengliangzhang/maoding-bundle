package com.maoding.notify.module.msgPusher;

import com.maoding.notify.module.message.dto.SendMessageDataDTO;

import java.util.Collection;
import java.util.List;

/**深圳市设计同道技术有限公司
 * 类    名：Msgpusher
 * 类描述：消息推送接口
 * 作    者：Chenxj
 * 日    期：2015年8月28日-下午2:48:01
 */
public interface MsgPusher {
	/**
	 * 方法描述：
	 * 作        者：Chenxj
	 * 日        期：2015年8月28日-下午3:29:36
	 * @param title
	 * @param content
	 */
    void pushSystemMsg(String title, String content);
	/**
	 * 方法描述：给用户绑定多个CID
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:14:12
	 * @param userid
	 * @param cids CID列表
	 * @return 只要有一个绑定成功则返回true
	 */
    boolean bindMultyCID(String userid, Collection<? extends Object> cids);
	/**
	 * 方法描述：给用户绑定多个CID
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午3:10:17
	 * @param userid
	 * @param cids
	 * @return
	 */
    boolean bindMultyCID(String userid, String... cids);
	/**
	 * 方法描述：根据用户ID查找CID
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:23:07
	 * @param userid
	 * @return
	 */
    List<String> findCIDbyUserid(String userid);
	/**
	 * 方法描述：根据CID查找用户ID
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:27:20
	 * @param cid
	 * @return
	 */
    String findUseridByCID(String cid);
	/**
	 * 方法描述：单个CID和别名解绑
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:32:33
	 * @param userid
	 * @param cid
	 * @return
	 */
    boolean unbindCID(String userid, String cid);
	/**
	 * 方法描述：取消用户所绑定的所有CID
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:34:39
	 * @param userid
	 * @return
	 */
    boolean unbindAllCID(String userid);
	/**
	 * 方法描述：给指定用户推送消息
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:40:45
	 * @param userid
	 * @param title
	 * @param content
	 */
    void pushMsgToUser(String userid, String title, String content);


	/**
	 * 方法描述：给指定用户推送消息
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:40:45
	 * @param userList
	 * @param title
	 * @param content
	 */
    void pushMsgToUserList(List<String> userList, String title, String content);


	/**
	 * 方法描述：给指定用户推送消息
	 * 作        者：Chenxj
	 * 日        期：2015年9月9日-下午2:40:45
	 */
	void pushRoleMsgToUserList(SendMessageDataDTO msg);
}
