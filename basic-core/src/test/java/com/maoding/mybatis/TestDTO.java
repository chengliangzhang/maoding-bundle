package com.maoding.mybatis;


import com.maoding.core.base.BaseIdObject;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/16
 * 类名: com.maoding.mybatis.TestDTO
 * 作者: 张成亮
 * 描述:
 **/
@Table(name = "md_web_task")
public class TestDTO extends BaseIdObject {
    /** id:任务编号 **/

    @Column(name = "path")
    private String taskPath;

    @Transient
    private String issuePath;

    public String getTaskPath() {
        return taskPath;
    }

    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }

    public String getIssuePath() {
        return issuePath;
    }

    public void setIssuePath(String issuePath) {
        this.issuePath = issuePath;
    }
}
