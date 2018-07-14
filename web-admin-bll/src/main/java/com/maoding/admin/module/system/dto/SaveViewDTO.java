package com.maoding.admin.module.system.dto;

import com.maoding.utils.StringUtils;

public class SaveViewDTO {

    private String id;

    private String name;

    private String pid;

    private String url;

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

    public String getPid() {
        if(StringUtils.isNullOrEmpty(pid)){
            pid = null;
        }
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
