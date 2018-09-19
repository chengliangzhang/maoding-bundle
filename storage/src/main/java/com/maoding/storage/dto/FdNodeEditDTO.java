package com.maoding.storage.dto;

import com.maoding.core.base.BaseDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/19
 * 类名: com.maoding.storage.dto.FdNodeEditDTO
 * 作者: 张成亮
 * 描述: 编辑文件或目录节点时的目标属性
 **/
public class FdNodeEditDTO extends BaseDTO {
    /** id:文件或目录节点编号 **/

    /** 文件或目录名 **/
    private String name;

    /** 父节点编号 **/
    private String pid;
    
    /** 节点路径名 **/
    private String path;
    
    /** 节点类型 **/
    private String type;

    /** 拥有者用户编号 **/
    private String ownerId;

    /** 所属项目编号 **/
    private String projectId;

    /** 所属任务编号 **/
    private String taskId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
