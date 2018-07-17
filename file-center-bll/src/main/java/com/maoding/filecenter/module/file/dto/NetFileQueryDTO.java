package com.maoding.filecenter.module.file.dto;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/17
 * @description :
 */
public class NetFileQueryDTO {
    /** 父节点编号 */
    private String pid;
    /** 要查找的节点名称 */
    private String fileName;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
