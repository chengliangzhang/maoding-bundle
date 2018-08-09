package com.maoding.constDefine.netFile;

/**
 * Created by Wuwq on 2017/2/15.
 */
public class NetFileType {

    /**
     * 目录
     */
    public static final Integer DIRECTORY = 0;

    /**
     * 文件
     */
    public static final Integer FILE = 1;

    /**
     * 项目附件（除项目文档库以为的附件） 附件类型(3.合同附件)
     */
    public static final Integer PROJECT_CONTRACT_ATTACH = 3;

    /**
     * 组织附件类型(4.公司logo,6.移动端上传轮播图片,7:公司邀请二维码)
     */
    public static final Integer COMPANY_LOGO_ATTACH = 4;

    /**
     * 组织附件类型(5：认证授权书)
     */
    public static final Integer CERTIFICATE_ATTACH = 5;

    /**
     * 组织附件类型(6.移动端上传轮播图片)
     */
    public static final Integer COMPANY_BANNER_ATTACH = 6;

    /**
     * 组织附件类型(7:公司邀请二维码)
     */
    public static final Integer COMPANY_QR_CODE_ATTACH = 7;

    /**
     * 组织附件类型(8:营业执照的类型)
     */
    public static final Integer BUSINESS_LICENSE_ATTACH = 8;

    /**
     * 组织附件类型(9:法人身份证信息)
     */
    public static final Integer LEGAL_REPRESENTATIVE_ATTACH = 9;

    /**
     * 组织附件类型(10：经办人身份证类型)
     */
    public static final Integer OPERATOR_ATTACH = 10;

    /**
     * 报销附件类型（20）
     */
    public static final Integer EXPENSE_ATTACH = 20;

    /**
     * 60：会议，日程文件
     */
    public static final Integer SCHEDULE_ATTACH = 60;

    /**
     * 通知公告附件类型（21）
     */
    public static final Integer NOTICE_ATTACH = 21;

    /**
     * 朋友圈、项目讨论组附近类型（22）
     */
    public static final Integer CIRCLE_ATTACH = 22;

    /**
     * 新建文件夹
     * */
    public static final Integer NEW_FILE =2;


    /**
     * 30：成果文件
     */
    public static final Integer DIRECTORY_ACHIEVEMENT = 30;

    /**
     * 40：归档通知(文件夹)
     */
    public static final Integer DIRECTORY_ARCHIVE_NOTICE = 40;

    /**
     * 50：发送归档通知(文件夹)
     */
    public static final Integer DIRECTORY_SEND_ARCHIVE_NOTICE = 50;

    /**
     * 收付款计划附件
     */
    public static final Integer FILE_TYPE_COST_PLAN_ATTACH = 70;

    /**
     * 100：个人(文件夹)
     */
    public static final Integer DIRECTORY_PERSONALLY = 100;

    /**
     * 101：个人(文件)
     */
    public static final Integer FILE_PERSONALLY = 101;

    /**
     * 201：临时文件
     */
    public static final int FILE_TYPE_TEMP = 201;

}
