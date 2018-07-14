package com.maoding.admin.module.historyData.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public class ImportResultDTO {
    /** 总导入记录数 */
    Integer totalCount;
    /** 有效记录列表 */
    List<Map<String,Object>> validList;
    /** 失败记录列表 */
    List<Map<String,Object>> invalidList;

    public void addTotalCount(){
        if (totalCount == null) totalCount = 0;
        totalCount++;
    }

    public void addInvalid(Map<String,Object> record){
        if (invalidList == null) invalidList = new ArrayList<Map<String,Object>>();
        invalidList.add(record);
    }
    
    public void addValid(Map<String,Object> record){
        if (validList == null) validList = new ArrayList<Map<String,Object>>();
        validList.add(record);
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getInvalidCount() {
        return (invalidList != null) ? invalidList.size() : 0;
    }

    public Integer getValidCount() {
        return (validList != null) ? validList.size() : 0;
    }

    public List<Map<String, Object>> getValidList() {
        return validList;
    }

    public void setValidList(List<Map<String, Object>> validList) {
        this.validList = validList;
    }

    public List<Map<String, Object>> getInvalidList() {
        return invalidList;
    }

    public void setInvalidList(List<Map<String, Object>> invalidList) {
        this.invalidList = invalidList;
    }
}
