package com.maoding.core.base;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/7
 * 类名: com.maoding.core.base.BaseIdObject
 * 作者: 张成亮
 * 描述: 包含编号的元素基类
 **/
public class BaseIdObject implements Serializable,Cloneable {
    /** 元素编号 */
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
