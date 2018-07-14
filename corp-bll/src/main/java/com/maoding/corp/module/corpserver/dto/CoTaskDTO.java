package com.maoding.corp.module.corpserver.dto;

import java.util.List;

/**
 * Created by Wuwq on 2017/5/5.
 */
public class CoTaskDTO {

    private int status;
    private String id;
    private String name;
    private String code;
    private String parentID;
    private String projectPhaseID;
    private String companyId;
    private int level;
    private int seq;
    private List<CoTaskMemberDTO> members;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getProjectPhaseID() {
        return projectPhaseID;
    }

    public void setProjectPhaseID(String projectPhaseID) {
        this.projectPhaseID = projectPhaseID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public List<CoTaskMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<CoTaskMemberDTO> members) {
        this.members = members;
    }

    public void addMember(CoTaskMemberDTO member) {
        this.members.add(member);
    }
}
