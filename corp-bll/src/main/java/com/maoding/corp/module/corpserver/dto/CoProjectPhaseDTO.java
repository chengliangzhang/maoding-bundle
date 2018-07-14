package com.maoding.corp.module.corpserver.dto;

import java.util.List;

/**
 * Created by Idccapp21 on 2017/3/14.
 */
public class CoProjectPhaseDTO {

    private String projectPhaseId;
    private int status;
    private String projectId;
    private String phaseName;
    private int level;
    private int seq;
    private List<CoTaskDTO> tasks;

    public String getProjectPhaseId() {
        return projectPhaseId;
    }

    public void setProjectPhaseId(String projectPhaseId) {
        this.projectPhaseId = projectPhaseId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
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

    public List<CoTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<CoTaskDTO> tasks) {
        this.tasks = tasks;
    }

    public void addTask(CoTaskDTO task) {
        this.tasks.add(task);
    }
}
