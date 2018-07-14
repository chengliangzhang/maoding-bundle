package com.maoding.admin.module.system.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_operate_permission")
public class OperatePermissionDO extends BaseEntity{

    private String code;

    private String name;

    private String url;

    private String pid;

    private Integer permissionType;

    private Integer moduleType;

    private Integer seq;

    private String forbidRelationTypeId;

    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getForbidRelationTypeId() {
        return forbidRelationTypeId;
    }

    public void setForbidRelationTypeId(String forbidRelationTypeId) {
        this.forbidRelationTypeId = forbidRelationTypeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
