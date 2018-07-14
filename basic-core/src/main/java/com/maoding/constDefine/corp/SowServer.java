package com.maoding.constDefine.corp;

/**
 * 第三方协同服务器
 */
public class SowServer {
    /**
     * 创建项目
     */
    public static final String URL_SET_PROJECT = "/Project/SetProject";

    /**
     * 发布项目（同步任务文件夹）
     */
    public static final String URL_PUBLIC_PROJECT = "/Project/PublicProject";


    /**
     * 创建项目的阶段
     */
    public static final String URL_SET_PROJECT_PHASE = "/Project/SetProjectPhase";


    /**
     * 创建多个人员
     */
    public static final String URL_SET_USERS = "/User/SetUsers";

    /**
     * 创建项目的任务
     */
    public static final String URL_SET_PROJECT_TASKS = "/Task/SetTasks";

    /**
     * 根据组织ID获取协同占用空间
     */
    public static final String URL_GET_SUM_BY_TEAM_ID = "/File/GetSumByTeamId";
//
//    /**
//     * 创建项目的任务
//     */
//    public static final String URL_SET_PROJECT_TASK ="/Task/SetTask";
//
//    /**
//     * 创建任务相关人员
//     */
//    public static final String URL_TASK_EDIT_MEMBER ="/Task/EditMember";
//
//    /**
//     * 创建任务的完成状态
//     */
//    public static final String URL_SET_TASK_PACKAGE_STATUS ="/Task/SetTaskPackageStatus";
//
//
//    /**
//     * 创建一个人员
//     */
//    public static final String URL_SET_USER ="/User/SetUser";


}
