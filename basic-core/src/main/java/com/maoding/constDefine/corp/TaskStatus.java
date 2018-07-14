package com.maoding.constDefine.corp;

/**
 * Created by Wuwq on 2017/2/15.
 * 任务状态
 */
public class TaskStatus {

    /**
     * 待执行
     */
    public static final int WaitRuning = 0;

    /**
     * 执行中
     */
    public static final int Running = 1;

    /**
     * 已完成
     */
    public static final int Finished = 2;

    /**
     * 重复任务，已跳过
     */
    public static final int SkipByDuplicate = 100;

    /**
     * 重试过多，已跳过
     */
    public static final int SkipByMaxRetry = 103;
}
