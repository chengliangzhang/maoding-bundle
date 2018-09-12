package com.maoding.core.base;

/**
 * @author  张成亮
 * @date    2018/7/27
 * @description     后台传递给前端用于显示的信息的基类
 **/
public class CoreShowDTO extends BaseIdObject {
    /** 元素内容（名称） */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
