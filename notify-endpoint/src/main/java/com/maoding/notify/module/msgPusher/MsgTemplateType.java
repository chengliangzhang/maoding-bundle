package com.maoding.notify.module.msgPusher;

/**深圳市设计同道技术有限公司
 * 类    名：MsgType
 * 类描述：推送消息模板类型
 * 作    者：Chenxj
 * 日    期：2015年9月9日-下午2:43:24
 */
public enum MsgTemplateType {
	/**点击通知打开应用模板*/
	NotificationTemplate,
	/**点击通知打开网页模板*/
	LinkTemplate,
	/**点击通知弹窗下载模板*/
	NotyPopLoadTemplate,
	/**透传消息模板*/
	TransmissionTemplate
}
