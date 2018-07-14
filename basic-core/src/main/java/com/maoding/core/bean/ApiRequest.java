package com.maoding.core.bean;

import com.maoding.core.base.BaseRequest;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/7/18.
 * 请求操作数据时用到的参数类型
 */
public final class ApiRequest<T> extends BaseRequest {
    /**
     * 目标数据，除id字段的其他字段为null则不操作，为0或""记录保存为指定值
     */
    T data;
    /**
     * 要更改或删除的记录的id，可以是多个
     */
    List<String> idList;

    /* 如果data内id字段为空，并且idList为空，则属于添加数据操作申请 */
    /* 如果data为空，并且idList不为空，则属于删除数据操作申请 */
    /* 如果data不为空，并且data内id不为空或idList不为空，则属于更改数据操作申请 */
    /* data内id不为空时，如果idList为空则只操作id指定的数据，如果idList不为空，则忽略id */

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
}
