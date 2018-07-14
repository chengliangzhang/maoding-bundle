package com.maoding.constDefine.corp;

/**
 * Created by Wuwq on 2017/2/15.
 * 同步状态
 */
public class SyncStatus {
    /** 待同步 */
    public static final int WaitSync = 0;

    /** 待重试 */
    public static final int WaitRetry = 1;

    /** 同步成功 */
    public static final int SyncSuccess = 2;

    /** 同步失败 */
    public static final int SyncFailed = 3;
}
