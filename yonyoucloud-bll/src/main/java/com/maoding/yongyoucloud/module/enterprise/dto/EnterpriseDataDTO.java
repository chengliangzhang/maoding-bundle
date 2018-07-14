package com.maoding.yongyoucloud.module.enterprise.dto;

import com.maoding.yongyoucloud.module.enterprise.model.EnterpriseDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnterpriseDataDTO extends EnterpriseDO{


    private String website;

    private String onlinetime;

    private List<Map<String,String>> persons = new ArrayList<>();
    private List<Map<String,String>> holders = new ArrayList<>();
    public List<Map<String, String>> getPersons() {
        return persons;
    }

    public void setPersons(List<Map<String, String>> persons) {
        this.persons = persons;
    }

    public List<Map<String, String>> getHolders() {
        return holders;
    }

    public void setHolders(List<Map<String, String>> holders) {
        this.holders = holders;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(String onlinetime) {
        this.onlinetime = onlinetime;
    }
}
