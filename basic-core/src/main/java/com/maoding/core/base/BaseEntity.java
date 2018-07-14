package com.maoding.core.base;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Wuwq on 2016/12/14.
 * 实体基类
 */
public class BaseEntity {

    @Id
    private String id;
    private String createBy;
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    /** 初始化实体 */
    public void initEntity() {
        resetId();
        resetCreateDate();
        resetUpdateDate();
    }

    /** 重置主键Id为新的UUID */
    public void resetId() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

    /** 重置创建时间为当前时间 */
    public void resetCreateDate() {
        this.createDate = LocalDateTime.now();
    }

    /** 重置更新时间为当前时间 */
    public void resetUpdateDate() {
        this.updateDate = LocalDateTime.now();
    }
}

