package com.maoding.filecenter.module.file.model;

import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;

/**
 * Created by Wuwq on 2017/05/31.
 */
@Table(name = "maoding_web_user_attach")
public class UserFileDO extends BaseEntity {

    private String userId;

    private String attachName;

    private String attachType;

    private String attachPath;
    /**
     *上传的分组
     */
    private String fileGroup;

    /**
     * oss 中的 bucket
     */
    private String bucket;

    /**
     * oss中的地址（只有bucket为公有访问的时候，该地址才有效
     */
    private String ossFilePath;

    /**
     * oos 中对应对象的key值
     */
    private String objectKey;


    public String getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName == null ? null : attachName.trim();
    }

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType == null ? null : attachType.trim();
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath == null ? null : attachPath.trim();
    }


    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOssFilePath() {
        return ossFilePath;
    }

    public void setOssFilePath(String ossFilePath) {
        this.ossFilePath = ossFilePath;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
}
