package com.maoding.admin.module.system.dto;

import java.util.ArrayList;
import java.util.List;

public class SaveViewRoleDTO {

    private String roleCode;

    private List<String> viewIdList = new ArrayList<String>();

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<String> getViewIdList() {
        return viewIdList;
    }

    public void setViewIdList(List<String> viewIdList) {
        this.viewIdList = viewIdList;
    }
}
