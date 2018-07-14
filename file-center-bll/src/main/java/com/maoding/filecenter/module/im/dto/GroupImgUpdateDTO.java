package com.maoding.filecenter.module.im.dto;

/**
 * Created by Wuwq on 2017/06/06.
 */
public class GroupImgUpdateDTO {
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    private String orgId;
    private String img;
    private Integer groupType;
}
