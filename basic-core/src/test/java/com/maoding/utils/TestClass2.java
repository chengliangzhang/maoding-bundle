package com.maoding.utils;

import com.maoding.core.base.BaseEntity;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/6/1 12:23
 * 描    述 :
 */
public class TestClass2 {
    private float digital;
    private Double objectDigital;
    private StringBuffer string;
    private Child object;
    private Byte[] array;
    private BaseEntity entity;
    private List<Float> list;

    public List<Float> getList() {
        return list;
    }

    public void setList(List<Float> list) {
        this.list = list;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    public Byte[] getArray() {
        return array;
    }

    public void setArray(Byte[] array) {
        this.array = array;
    }

    public Child getObject() {
        return object;
    }

    public void setObject(Child object) {
        this.object = object;
    }

    public float getDigital() {
        return digital;
    }

    public void setDigital(float digital) {
        this.digital = digital;
    }

    public Double getObjectDigital() {
        return objectDigital;
    }

    public void setObjectDigital(Double objectDigital) {
        this.objectDigital = objectDigital;
    }

    public StringBuffer getString() {
        return string;
    }

    public void setString(StringBuffer string) {
        this.string = string;
    }
}
