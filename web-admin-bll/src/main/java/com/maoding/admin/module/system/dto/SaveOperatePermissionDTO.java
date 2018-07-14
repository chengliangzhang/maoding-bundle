package com.maoding.admin.module.system.dto;

public class SaveOperatePermissionDTO {

    private String id;

    private String viewId;

    private String viewCode;

    private String name;

    private String description;

    private Integer permissionType;

    private Integer seq;

    private String forbidRelationTypeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
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

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
