package com.maoding.mybatis;

import com.maoding.core.base.CoreEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/10
 * 类名: com.maoding.mybatis.TestCoreEntity
 * 作者: 张成亮
 * 描述:
 **/
@Table(name = "md_tree_storage")
public class TestCoreEntity extends CoreEntity {
    @Column
    private String name;

    @Column
    private String path;

    @Transient
    private String companyId;

    @Transient
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
