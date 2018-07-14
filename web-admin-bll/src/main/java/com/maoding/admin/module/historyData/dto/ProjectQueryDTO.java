package com.maoding.admin.module.historyData.dto;

import com.maoding.core.base.BaseQueryDTO;

import java.time.LocalDateTime;

/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public class ProjectQueryDTO extends BaseQueryDTO{
    /** 立项组织 */
    String companyId;
    /** 项目编号 */
    String projectNo;
    /** 项目名称 */
    String projectName;
    /** 立项日期 */
    LocalDateTime createDate;

    public ProjectQueryDTO(){}
    public ProjectQueryDTO(String companyId,String projectNo,String projectName,LocalDateTime createDate){
        setCompanyId(companyId);
        setProjectNo(projectNo);
        setProjectName(projectName);
        setCreateDate(createDate);
        setPageSize(1);
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
