package com.maoding.admin.module.historyData.service;

import com.maoding.admin.constDefine.ProjectConst;
import com.maoding.admin.module.historyData.dao.*;
import com.maoding.admin.module.historyData.dto.ImportResultDTO;
import com.maoding.admin.module.historyData.dto.MemberQueryDTO;
import com.maoding.admin.module.historyData.dto.ProjectQueryDTO;
import com.maoding.admin.module.historyData.model.ProjectAuditDO;
import com.maoding.admin.module.historyData.model.ProjectDO;
import com.maoding.admin.module.historyData.model.ProjectMemberDO;
import com.maoding.core.base.BaseService;
import com.maoding.utils.DateUtils;
import com.maoding.utils.ExcelUtils;
import com.maoding.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/7/19.
 * 导入文件服务
 */
@Service("importService")
public class ImportServiceImpl extends BaseService implements ImportService {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);

    @Autowired
    CompanyDAO companyDAO;

    @Autowired
    UserDAO userReadOnlyDAO;

    @Autowired
    ProjectDAO projectDAO;

    @Autowired
    ProjectMemberDAO projectMemberDAO;

    @Autowired
    ProjectAuditDAO projectAuditDAO;

    LocalDate contractDate; //临时存放合同签订日期

    /**
     * 导入项目数据
     *
     * @param in 输入流
     * @param token 操作用户标识
     * @return 导入结果
     */
    @Override
    public ImportResultDTO importProjects(InputStream in, String token) {
        if (in == null) throw new IllegalArgumentException("importProjects 参数错误");

        final Integer DEFAULT_SHEET_INDEX = -1;
        final Integer DEFAULT_TITLE_ROW = 4;
        List<Map<String,Object>> dataList = ExcelUtils.readFrom(ExcelUtils.getWorkbook(in),DEFAULT_SHEET_INDEX,DEFAULT_TITLE_ROW);
        if ((dataList == null) || (dataList.isEmpty())) throw new IllegalArgumentException("importProjects 找不到有效数据");

        ImportResultDTO result = new ImportResultDTO();
        for (Map<String,Object> data : dataList){
            result.addTotalCount();

            ProjectDO project = createProjectDOFrom(data,token);
            //检查数据有效性
            if ((project == null)
                    || ((project.getProjectNo() == null) && (ProjectConst.PROJECT_NO.contains("*")))
                    || ((project.getProjectName() == null) && (ProjectConst.PROJECT_NAME.contains("*")))
                    || ((project.getCompanyId() == null) && (ProjectConst.PROJECT_COMPANY_NAME.contains("*")))
                    || ((project.getCreateBy() == null) && (ProjectConst.PROJECT_CREATOR_NAME.contains("*")))
                    || ((project.getCreateDate() == null) && (ProjectConst.PROJECT_CREATE_DATE.contains("*")))){
                result.addInvalid(data);
            } else {
                result.addValid(data);
            }
        }
        return result;
    }

    @Override
    public ImportResultDTO importProjects(List<Map<String, Object>> dataList, String token) {
        if (dataList == null) throw new IllegalArgumentException("importProjects 参数错误");

        ImportResultDTO result = new ImportResultDTO();
        for (Map<String,Object> data : dataList) {
            result.addTotalCount();

            ProjectDO project = createProjectDOFrom(data, token);
            //检查数据有效性
            if ((project == null)
                    || ((project.getProjectNo() == null) && (ProjectConst.PROJECT_NO.contains("*")))
                    || ((project.getProjectName() == null) && (ProjectConst.PROJECT_NAME.contains("*")))
                    || ((project.getCompanyId() == null) && (ProjectConst.PROJECT_COMPANY_NAME.contains("*")))
                    || ((project.getCreateBy() == null) && (ProjectConst.PROJECT_CREATOR_NAME.contains("*")))
                    || ((project.getCreateDate() == null) && (ProjectConst.PROJECT_CREATE_DATE.contains("*")))) {
                result.addInvalid(data);
                continue;
            }

            //存储项目数据
            ProjectQueryDTO query = new ProjectQueryDTO(project.getCompanyId(), project.getProjectNo(), project.getProjectName(), project.getCreateDate());
            if (projectDAO.getProject(query) == null) {
                try {
                    insertProject(project);
                } catch (Exception e) {
                    log.error("添加数据时产生错误", e);
                    result.addInvalid(data);
                    continue;
                }
            } else {
                result.addInvalid(data);
                continue;
            }
            result.addValid(data);
        }
        return result;
    }

    /** 添加项目 */
    private void insertProject(ProjectDO project){
        if (project == null) return;
        //补充缺失字段
        if (project.getId() == null) project.resetId();
        if (project.getPstatus() == null) project.setPstatus("0");
        if (project.getCreateDate() == null) project.resetCreateDate();

        //添加立项人和项目负责人
        if (!StringUtils.isEmpty(project.getCompanyId())){
            insertProjectMember(project.getCompanyId(),project.getId(), ProjectConst.MEMBER_TYPE_CREATOR,project.getCreateBy());
            insertProjectMember(project.getCompanyId(),project.getId(), ProjectConst.MEMBER_TYPE_MANAGER,project.getCreateBy());
            insertProjectMember(project.getCompanyId(),project.getId(), ProjectConst.MEMBER_TYPE_DESIGN,project.getCreateBy());
        }
        //添加乙方项目负责人
        if (!StringUtils.isEmpty(project.getCompanyBid())){
            insertProjectMember(project.getCompanyBid(),project.getId(), ProjectConst.MEMBER_TYPE_MANAGER,project.getCreateBy());
            insertProjectMember(project.getCompanyBid(),project.getId(), ProjectConst.MEMBER_TYPE_DESIGN,project.getCreateBy());
        }
        //保存合同签订日期
        ProjectAuditDO audit = new ProjectAuditDO();
        audit.resetId();
        audit.setProjectId(project.getId());
        audit.setAuditDate(contractDate);
        audit.setAuditType("2");
        audit.setCreateBy(project.getCreateBy());
        audit.setCreateDate(project.getCreateDate());
        projectAuditDAO.insert(audit);
        //添加项目数据
        projectDAO.insert(project);
    }

    /** 添加项目成员 */
    private void insertProjectMember(String companyId, String projectId, Integer memberType, String userId){
        if ((companyId == null) || (projectId == null) || (memberType == null)) return;
        MemberQueryDTO query = new MemberQueryDTO(companyId,projectId,memberType);
        if (projectMemberDAO.getProjectMember(query) == null) {
            ProjectMemberDO member = new ProjectMemberDO();
            member.resetId();
            member.setProjectId(projectId);
            member.setCompanyId(companyId);
            member.setAccountId(userId);
            //在当前用户无权限时，更改为默认的项目负责人
            String permissionId = ProjectConst.PERMISSION_MAPPER.get(memberType);
            if (permissionId != null) {
                List<String> list = companyDAO.listUserIdByCompanyIdAndPermissionId(companyId,permissionId);
                if ((userId == null) || ((list != null) && !(list.contains(userId)))) {
                    member.setAccountId(list.get(0));
                }
            }
            if (!StringUtils.isEmpty(member.getCompanyId()) && (!StringUtils.isEmpty(member.getAccountId()))) {
                member.setCompanyUserId(companyDAO.getCompanyUserIdByCompanyIdAndUserId(member.getCompanyId(),member.getAccountId()));
            }
            member.setMemberType(memberType);
            member.setStatus(0);
            member.setDeleted(0);
            member.setSeq(0);
            member.resetCreateDate();
            member.setCreateBy(userId);
            projectMemberDAO.insert(member);
        }
    }

    /** 转换数据为实体对象 */
    private ProjectDO createProjectDOFrom(Map<String,Object> data, String token){
        if (data == null) return null;

        ProjectDO project = new ProjectDO();

        //项目编号
        project.setProjectNo((String)data.get(ProjectConst.PROJECT_NO));

        //项目名称
        project.setProjectName((String)data.get(ProjectConst.PROJECT_NAME));

        //立项组织和立项人
        String creatorCompanyName = (String)data.get(ProjectConst.PROJECT_COMPANY_NAME);
        String creatorUserName = (String)data.get(ProjectConst.PROJECT_CREATOR_NAME);
        if (StringUtils.isEmpty(creatorCompanyName) || StringUtils.isEmpty(creatorUserName)) return null;

        String creatorCompanyId = companyDAO.getCompanyIdByCompanyNameAndUserName(creatorCompanyName, creatorUserName);
        project.setCompanyId(creatorCompanyId);
        String creatorUserId = userReadOnlyDAO.getUserIdByCompanyNameAndUserName(creatorCompanyName, creatorUserName);
        project.setCreateBy(creatorUserId);

        //立项日期
        project.setCreateDate(DateUtils.getLocalDateTime((Date)data.get(ProjectConst.PROJECT_CREATE_DATE)));

        //合同签订日期
        contractDate = DateUtils.getLocalDate((Date)data.get(ProjectConst.PROJECT_CONTRACT_DATE));
        //TODO 目前project内不存在合同签订日期字段，需要临时保存，并保存到maoding_web_project_audit

        //项目地点-省/直辖市
        project.setProvince((String)data.get(ProjectConst.PROJECT_PROVINCE));

        //项目地点-市/直辖市区
        project.setCity((String)data.get(ProjectConst.PROJECT_CITY));

        //项目地点-区/县
        project.setCounty((String)data.get(ProjectConst.PROJECT_COUNTY));

        //项目地点-详细地址
        project.setDetailAddress((String)data.get(ProjectConst.PROJECT_DETAIL_ADDRESS));

        //项目状态
        String status = (String)data.get(ProjectConst.PROJECT_STATUS);
        if ((status != null) && ProjectConst.STATUS_MAPPER.containsKey(status)){
            project.setStatus(ProjectConst.STATUS_MAPPER.get(status));
        } else {
            project.setStatus(ProjectConst.PROJECT_STATUS_FINISHED);
        }

        //甲方
        String aCompanyName = (String)data.get(ProjectConst.PROJECT_A_NAME);
        if (!StringUtils.isEmpty(aCompanyName)) {
            String aCompanyId = companyDAO.getCompanyIdByCompanyNameForA(aCompanyName,creatorCompanyId);
            project.setConstructCompany(aCompanyId);
        }

        //乙方
        String bCompanyName = (String)data.get(ProjectConst.PROJECT_B_NAME);
        if (!StringUtils.isEmpty(bCompanyName)) {
            String bCompanyId = companyDAO.getCompanyIdByCompanyNameForB(bCompanyName,creatorCompanyId);
            project.setCompanyBid(bCompanyId);
        }

        //建筑类型
        project.setProjectType("1c6f48757e684b3cb059b94021e12baa");

        return project;
    }
}
