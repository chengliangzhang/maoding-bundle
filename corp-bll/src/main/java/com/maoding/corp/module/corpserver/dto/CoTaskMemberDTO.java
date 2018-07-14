package com.maoding.corp.module.corpserver.dto;

/**
 * Created by Wuwq on 2017/5/5.
 */
public class CoTaskMemberDTO {

    private String id;
    private String role;
    private String peerID;
    private int state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPeerID() {
        return peerID;
    }

    public void setPeerID(String peerID) {
        this.peerID = peerID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
