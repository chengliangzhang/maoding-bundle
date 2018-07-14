package com.maoding.core.base;

import java.io.Serializable;

/**
 * Created by Chengliang.zhang on 2017/7/18.
 * 操作请求基类
 */
public class BaseRequest implements Serializable {
    /** 操作者标识，目前使用String类型，以后可转换为token类型 */
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
