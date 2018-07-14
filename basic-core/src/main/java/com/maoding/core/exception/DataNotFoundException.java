package com.maoding.core.exception;

/**
 * Created by Wuwq on 2017/05/31.
 * 找不到本该找到的数据时触发的异常
 */
public class DataNotFoundException extends Exception {

    public DataNotFoundException(String message) {
        super(message);
    }
}
