package com.maoding.filecenter.module.file.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

@Table(name = "maoding_web_net_file_crt")
public class NetFileCrtDO extends BaseEntity{

    /**
     * 状态（0：有效，1：无效（删除））
     */
    private Integer deleted;

    /**
     * 缩略图地址
     */
    private String crtFilePath;

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCrtFilePath() {
        return crtFilePath;
    }

    public void setCrtFilePath(String crtFilePath) {
        this.crtFilePath = crtFilePath;
    }
}
