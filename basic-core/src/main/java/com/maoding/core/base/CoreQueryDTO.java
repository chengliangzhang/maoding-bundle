package com.maoding.core.base;


import com.maoding.utils.DigitUtils;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/3
 * 类名: com.maoding.core.base.dto.CoreQueryDTO
 * 作者: 张成亮
 * 描述: 用于查询信息的条件
 **/
public class CoreQueryDTO extends BaseDTO {
    //默认页面大小
    private static final int DEFAULT_PAGE_SIZE = 20;

    /** 要查找多个元素时，目标元素编号列表 */
    private List<String> idList;

    /** 分页查找时指定的页码，如果不进行分页查找，设置为null */
    private Integer pageIndex;

    /** 分页查找时指定的页大小，如果页码为空，此信息无效 */
    private Integer pageSize;

    /** 分页查找时的起始行 */
    private Integer startLine;

    public Integer getStartLine() {
        Integer startLine = this.startLine;
        if ((startLine == null) && (getPageIndex() != null)) {
            int size = (getPageSize() != null) ? DigitUtils.parseInt(getPageSize()) : DEFAULT_PAGE_SIZE;
            return DigitUtils.parseInt(getPageIndex()) * size;
        }
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
