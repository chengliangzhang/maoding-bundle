package com.maoding.utils;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/6/1 12:24
 * 描    述 :
 */
public class Child extends Father{
    private String s;

    public Child(){}
    public Child(Integer i, String s){
        super(i);
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
