package com.maoding.mybatis;


import com.maoding.core.base.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/16
 * 类名: com.maoding.mybatis.TestQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class TestQueryDTO extends CoreQueryDTO {
    private String name;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
