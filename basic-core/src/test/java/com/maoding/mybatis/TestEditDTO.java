package com.maoding.mybatis;

import com.maoding.core.base.CoreEditDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/17
 * 类名: com.maoding.mybatis.TestEditDTO
 * 作者: 张成亮
 * 描述:
 **/
public class TestEditDTO extends CoreEditDTO {
    /** id: 任务编号 */

    private String name;

    private String path;

    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
