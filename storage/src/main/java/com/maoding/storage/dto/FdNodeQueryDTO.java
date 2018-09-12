package com.maoding.storage.dto;

import com.maoding.core.base.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/12
 * 类名: com.maoding.storage.dto.FileQueryDTO
 * 作者: 张成亮
 * 描述:
 **/
public class FdNodeQueryDTO extends CoreQueryDTO {
    /** id:文件节点编号 **/

    /** 父节点编号 **/
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
