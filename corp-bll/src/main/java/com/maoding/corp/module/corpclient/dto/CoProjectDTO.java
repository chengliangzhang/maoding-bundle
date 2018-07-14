package com.maoding.corp.module.corpclient.dto;

/**
 * Created by Idccapp21 on 2017/3/14.
 */
public class CoProjectDTO {

    private String id;

    private String code;

    private String name;

    private int status;

    private String owerUserId;

    private String belongBranchName;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getOwerUserId() {
        return owerUserId;
    }

    public void setOwerUserId(String owerUserId) {
        this.owerUserId = owerUserId;
    }

    public String getBelongBranchName() {
        return belongBranchName;
    }

    public void setBelongBranchName(String belongBranchName) {
        this.belongBranchName = belongBranchName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
