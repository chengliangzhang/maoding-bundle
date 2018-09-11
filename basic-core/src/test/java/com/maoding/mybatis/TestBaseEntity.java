package com.maoding.mybatis;


import com.maoding.core.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/16
 * 类名: com.maoding.mybatis.provider.TestDTO
 * 作者: 张成亮
 * 描述:
 **/
@Table(name = "maoding_web_project_task")
public class TestBaseEntity extends BaseEntity {
    @Column(name = "task_name")
    private String name;

    @Column(name = "task_status")
    private String status;

    @Column
    private String projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
