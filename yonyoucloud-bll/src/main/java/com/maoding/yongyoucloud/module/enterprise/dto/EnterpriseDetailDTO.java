package com.maoding.yongyoucloud.module.enterprise.dto;

import java.util.List;

public class EnterpriseDetailDTO {

    private String result;

    private String state;

    private Integer total;

    private List<EnterpriseDataDTO> details;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<EnterpriseDataDTO> getDetails() {
        return details;
    }

    public void setDetails(List<EnterpriseDataDTO> details) {
        this.details = details;
    }
}
