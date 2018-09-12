package com.maoding.storage.dto;

import com.maoding.core.base.BaseIdObject;

import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/12
 * 类名: com.maoding.storage.dto.FileDTO
 * 作者: 张成亮
 * 描述:
 **/
@Table(name = "md_node_storage")
public class FdNodeDTO extends BaseIdObject {
    /** id:文件或目录节点编号 **/

    /** 文件或目录名 **/
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
