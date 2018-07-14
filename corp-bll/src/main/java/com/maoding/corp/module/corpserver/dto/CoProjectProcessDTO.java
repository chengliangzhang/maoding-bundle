package com.maoding.corp.module.corpserver.dto;

import java.util.List;


/**
 * Created by Wuwq on 2016/10/27.
 */
public class CoProjectProcessDTO {

    private String id;
    private String processName;
    private String companyId;
    private String projectId;
    private String taskManageId;
    private String status;
    private int seq;
    private List<CoProjectProcessNodeDTO> nodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskManageId() {
        return taskManageId;
    }

    public void setTaskManageId(String taskManageId) {
        this.taskManageId = taskManageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public List<CoProjectProcessNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<CoProjectProcessNodeDTO> nodes) {
        this.nodes = nodes;
    }
}
