package com.maoding.core.base;

import com.maoding.utils.BeanUtils;
import com.maoding.utils.StringUtils;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: CoreDTO
 * @description : 元素基类
 */
public class BaseIdObject implements Serializable,Cloneable {
    /** 元素编号 */
    @Id
    private String id;

    /** 路径字段名,用于更新保存树结构数据的表 **/
    @Transient
    private String pathField;

    /** 路径内分隔字符串，用于保存树结构数据内树路径，原始表结构内通常为"-"，新表接口内通常为"/" **/
    @Transient
    private String pathSplit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 判断是否存在路径字段，及路径字段是否为空
     **/
    public boolean isPathEmpty(){
        return StringUtils.isEmpty(getPathField())
                || StringUtils.isEmpty(BeanUtils.getProperty(this,getPathField(),String.class,true));
    }

    public void setPathField(String pathField) {
        this.pathField = pathField;
    }

    public String getPathField() {
        return pathField;
    }

    public void setPathSplit(String pathSplit) {
        this.pathSplit = pathSplit;
    }

    public String getPathSplit() {
        return pathSplit;
    }
}
