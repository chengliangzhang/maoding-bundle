package com.maoding.admin.module.system.dto;

import java.util.ArrayList;
import java.util.List;

public class ViewOperatorDTO {

    private String id;

    private String viewName;

    private String code;

    private Integer isSelect;

    List<ViewOperatorDTO> childList = new ArrayList<>();

    List<OperateDTO> operateList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ViewOperatorDTO> getChildList() {
        return childList;
    }

    public void setChildList(List<ViewOperatorDTO> childList) {
        this.childList = childList;
    }

    public List<OperateDTO> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<OperateDTO> operateList) {
        this.operateList = operateList;
    }

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }
}
