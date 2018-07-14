package com.maoding.admin.module.system.dto;

public class OperateDTO {

    private String id;

    private String operateName;

    private Integer seq;

    private Integer operateSelect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getOperateSelect() {
        return operateSelect;
    }

    public void setOperateSelect(Integer operateSelect) {
        this.operateSelect = operateSelect;
    }
}
