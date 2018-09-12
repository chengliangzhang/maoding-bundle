package com.maoding.core.base;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/9/12 19:12
 * 描    述 :
 */
public class CoreEntity extends BaseIdObject implements Serializable,Cloneable {
    /** 删除标志 */
    @Column
    private Short deleted;

    /** 创建时间 */
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 最后修改时间 */
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifyTime;

    /** 最后修改者ID */
    @Column
    private String lastModifyUserId;

    /** 最后修改者角色ID */
    @Column
    private String lastModifyRoleId;

    /** 重新初始化 */
    public void reset() {
        resetId();
        resetTime();
    }

    /** 准备更新 */
    public void update() {
        resetDeleted();
        resetLastModifyTime();
    }

    /** 清理被初始化的字段 */
    public void clear() {
        setId(null);
        setCreateTime(null);
        setLastModifyTime(null);
    }

    /** 重新初始化时间 */
    public void resetTime(){
        resetDeleted();
        resetCreateTime();
        resetLastModifyTime();
    }

    /** 重置主键Id为新的UUID */
    public void resetId() {
        setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    /** 重置删除标志 */
    public void resetDeleted(){
        setDeleted((short)0);
    }

    /** 重置创建时间为当前时间 */
    public void resetCreateTime() {
        setCreateTime(new Date());
    }

    /** 重置最后更改时间为当前时间 */
    public void resetLastModifyTime() {
        setLastModifyTime(new Date());
    }

    public Short getDeleted() {
        return deleted;
    }

    public void setDeleted(Short deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public String getLastModifyRoleId() {
        return lastModifyRoleId;
    }

    public void setLastModifyRoleId(String lastModifyRoleId) {
        this.lastModifyRoleId = lastModifyRoleId;
    }
}

