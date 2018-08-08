package com.maoding.utils;

import com.maoding.core.base.BaseEntity;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/6/1 12:22
 * 描    述 :
 */
public class TestClass1 {
    private int digital;
    private Long objectDigital;
    private String string;
    private Father object;
    private byte[] array;
    private BaseEntity entity;
    private List<Integer> list;

    public TestClass1() {}

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    public byte[] getArray() {
        return array;
    }

    public void setArray(byte[] array) {
        this.array = array;
    }

    public Father getObject() {
        return object;
    }

    public void setObject(Father object) {
        this.object = object;
    }

    public int getDigital() {
        return digital;
    }

    public void setDigital(int digital) {
        this.digital = digital;
    }

    public Long getObjectDigital() {
        return objectDigital;
    }

    public void setObjectDigital(Long objectDigital) {
        this.objectDigital = objectDigital;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
