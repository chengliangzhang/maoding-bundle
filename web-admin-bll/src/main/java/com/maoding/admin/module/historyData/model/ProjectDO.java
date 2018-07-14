package com.maoding.admin.module.historyData.model;


import com.maoding.core.base.BaseEntity;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectEntity
 * 类描述：权限实体
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
@Table(name = "maoding_web_project")
public class ProjectDO extends BaseEntity {
    /**
     * 企业id
     */
    private String companyId;

    /**
     * 挂靠的大B id
     */
    private String companyBid;

    /**
     * 项目类别(冗余，目前没用到)
     */
    private String projectType;

    /**
     * 建筑功能
     */
    private String builtType;

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 基地面积
     */
    private String baseArea;

    /**
     * 计容面积
     */
    private String capacityArea;

    /**
     * 总建筑面积
     */
    private String totalConstructionArea;

    /**
     * 核增面积
     */
    private String increasingArea;

    /**
     * 覆盖率
     */
    private String coverage;

    /**
     * 绿化率
     */
    private String greeningRate;

    /**
     * 建筑高度
     */
    private String builtHeight;

    /**
     * 建筑层数(地上)
     */
    private String builtFloorUp;

    /**
     * 建筑层数(地下)
     */
    private String builtFloorDown;

    /**
     * 建设单位
     */
    private String constructCompany;

    /**
     * 投资估算
     */
    private BigDecimal investmentEstimation;

    /**
     * 合同总金额
     */
    private BigDecimal totalContractAmount;

    /**
     * 默认为0(1=拟定，2=评审，3=备案，4.签发管理任务书，5.签发设计任务书)
     */
    private String status;

    /**
     * 0=生效，1=不生效
     */
    private String pstatus;

    /**
     * 合同附件
     */
    private String contractAttachment;

    /**
     * 设计依据
     */
    private String designBasis;

    /**
     * 设计范围
     */
    private String designRange;

    /**
     * 合作设计费审批流程id
     */
    private String processId;

    /**
     * 项目拆分后的父Id
     */
    private String parentProjectid;

    /**
     * 企业所属省
     */
    private String province;

    /**
     * 企业所属市
     */
    private String city;

    /**
     * 企业所属县或区或镇
     */
    private String county;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 容积率
     */
    private String volumeRatio;

    /**
     *项目经营人（也可以移交给其他人）
     */
    private String managerId;

    public ProjectDO() {
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyBid() {
        return companyBid;
    }

    public void setCompanyBid(String companyBid) {
        this.companyBid = companyBid;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getBuiltType() {
        return builtType;
    }

    public void setBuiltType(String builtType) {
        this.builtType = builtType;
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

    public String getBaseArea() {
        return baseArea;
    }

    public void setBaseArea(String baseArea) {
        this.baseArea = baseArea;
    }

    public String getCapacityArea() {
        return capacityArea;
    }

    public void setCapacityArea(String capacityArea) {
        this.capacityArea = capacityArea;
    }

    public String getTotalConstructionArea() {
        return totalConstructionArea;
    }

    public void setTotalConstructionArea(String totalConstructionArea) {
        this.totalConstructionArea = totalConstructionArea;
    }

    public String getIncreasingArea() {
        return increasingArea;
    }

    public void setIncreasingArea(String increasingArea) {
        this.increasingArea = increasingArea;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getGreeningRate() {
        return greeningRate;
    }

    public void setGreeningRate(String greeningRate) {
        this.greeningRate = greeningRate;
    }

    public String getBuiltHeight() {
        return builtHeight;
    }

    public void setBuiltHeight(String builtHeight) {
        this.builtHeight = builtHeight;
    }

    public String getBuiltFloorUp() {
        return builtFloorUp;
    }

    public void setBuiltFloorUp(String builtFloorUp) {
        this.builtFloorUp = builtFloorUp;
    }

    public String getBuiltFloorDown() {
        return builtFloorDown;
    }

    public void setBuiltFloorDown(String builtFloorDown) {
        this.builtFloorDown = builtFloorDown;
    }

    public String getConstructCompany() {
        return constructCompany;
    }

    public void setConstructCompany(String constructCompany) {
        this.constructCompany = constructCompany;
    }

    public BigDecimal getInvestmentEstimation() {
        return investmentEstimation;
    }

    public void setInvestmentEstimation(BigDecimal investmentEstimation) {
        this.investmentEstimation = investmentEstimation;
    }

    public BigDecimal getTotalContractAmount() {
        return totalContractAmount;
    }

    public void setTotalContractAmount(BigDecimal totalContractAmount) {
        this.totalContractAmount = totalContractAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getContractAttachment() {
        return contractAttachment;
    }

    public void setContractAttachment(String contractAttachment) {
        this.contractAttachment = contractAttachment;
    }

    public String getDesignBasis() {
        return designBasis;
    }

    public void setDesignBasis(String designBasis) {
        this.designBasis = designBasis;
    }

    public String getDesignRange() {
        return designRange;
    }

    public void setDesignRange(String designRange) {
        this.designRange = designRange;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getParentProjectid() {
        return parentProjectid;
    }

    public void setParentProjectid(String parentProjectid) {
        this.parentProjectid = parentProjectid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVolumeRatio() {
        return volumeRatio;
    }

    public void setVolumeRatio(String volumeRatio) {
        this.volumeRatio = volumeRatio;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
