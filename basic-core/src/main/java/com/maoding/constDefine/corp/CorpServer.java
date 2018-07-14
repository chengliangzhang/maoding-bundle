package com.maoding.constDefine.corp;

/**
 * 卯丁协同服务器
 */
public class CorpServer {

    public static final String URL_LOGIN = "/collaboration/login";

    public static final String URL_GET_COMPANY_DISK_INFO="/companyDisk/getCompanyDiskInfo";

    public static final String URL_GET_UPDATE_CORP_SIZE="/companyDisk/updateCorpSizeOnCompanyDisk";

    public static final String URL_LIST_COMPANY_BY_IDS = "/collaboration/listCompanyByIds";

    public static final String URL_LIST_USER_BY_COMPANY_ID = "/collaboration/listUserByCompanyId";

    public static final String URL_LIST_PROJECT_BY_COMPANY_ID = "/collaboration/listProjectByCompanyId";

    public static final String URL_GET_PROJECT_BY_ID = "/collaboration/getProjectById";

    public static final String URL_LIST_NODE = "/collaboration/listNode";

    public static final String URL_HANDLE_MY_TASK_BY_PROJECT_NODE_ID = "/collaboration/handleMyTaskByProjectNodeId";

    public static final String URL_GET_SYNC_COMPANY="/syncCompany/select";

    public static final String URL_SYNC_ALL_BY_ENDPOINT="/collaboration/syncAllByEndpoint";
}
