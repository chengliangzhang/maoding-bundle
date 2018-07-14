package com.maoding.admin.module.orgAuth.dto;

/**
 * Created by Chengliang.zhang on 2017/7/13.
 */
public class OrgAuthAttachDTO {
    /**
     * 组织ID
     */
    private String companyId;
    /**
     * 类型参考，同NetFileDO.type
     */
    private Integer type;
    /**
     * 上传的分组，同NetFileDO.fileGroup
     */
    private String fileGroup;
    /**
     * 文件在fastdfs中的路径 同NetFileDO.filePath
     */
    private String filePath;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
