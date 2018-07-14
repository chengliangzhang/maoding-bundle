package com.maoding.admin.module.historyData.dto;

import com.maoding.core.base.BaseQueryDTO;

/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public class MemberQueryDTO extends BaseQueryDTO {
    /** 人员所在公司id */
    String companyId;
    /** 人员所在项目id */
    String projectId;
    /** 人员类型 */
    Integer memberType;

    public MemberQueryDTO(){}
    public MemberQueryDTO(String companyId,String projectId,Integer memberType){
        setCompanyId(companyId);
        setProjectId(projectId);
        setMemberType(memberType);
        setPageSize(1);
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }
}
