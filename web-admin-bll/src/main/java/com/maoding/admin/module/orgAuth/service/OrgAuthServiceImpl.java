package com.maoding.admin.module.orgAuth.service;

import com.maoding.admin.config.FastdfsConfig;
import com.maoding.admin.module.orgAuth.dao.CommonDAO;
import com.maoding.admin.module.orgAuth.dao.OrgAuthAuditDAO;
import com.maoding.admin.module.orgAuth.dao.OrgAuthDAO;
import com.maoding.admin.module.orgAuth.dto.*;
import com.maoding.admin.module.orgAuth.model.OrgAuthAuditDO;
import com.maoding.admin.module.orgAuth.model.OrgAuthDO;
import com.maoding.constDefine.netFile.NetFileType;
import com.maoding.core.base.BaseService;
import com.maoding.utils.BeanUtils;
import com.maoding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Wuwq on 2017/07/11.
 */
@Service("orgAuthService")
public class OrgAuthServiceImpl extends BaseService implements OrgAuthService {

    @Autowired
    private FastdfsConfig fastdfsConfig;

    @Autowired
    private OrgAuthDAO orgAuthDAO;

    @Autowired
    private OrgAuthAuditDAO orgAuthAuditDAO;

    @Autowired
    private CommonDAO commonMapper;

    /**
     * 方法：设置免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId 组织ID
     * @param expiryDate 免费到期日期
     */
    @Override
    public void setExpiryDate(String orgId, LocalDateTime expiryDate) {
        setExpiryDate(orgId,expiryDate,null);
    }

    /**
     * 方法：设置免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId 组织ID
     * @param expiryDate 免费到期日期
     * @param operatorUserId 操作者ID
     */
    @Override
    public void setExpiryDate(String orgId, LocalDateTime expiryDate, String operatorUserId) {
        if ((orgId == null) || (expiryDate == null)) throw new NullPointerException("setExpiryDate 参数错误");

        OrgAuthDO entity = new OrgAuthDO();
        entity.setId(orgId);
        entity.setExpiryDate(expiryDate);
        entity.setUpdateBy(operatorUserId);
        entity.setUpdateDate(LocalDateTime.now());
        orgAuthDAO.updateByPrimaryKey(entity);
    }

    /**
     * 方法：延长免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId
     * @param days
     */
    @Override
    public void extendExpiryDate(String orgId, Integer days) {
        extendExpiryDate(orgId, days, null);
    }

    /**
     * 方法：延长免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId 组织ID
     * @param days 延期时长
     * @param operatorUserId 操作者ID
     */
    @Override
    public void extendExpiryDate(String orgId, Integer days, String operatorUserId) {
        if ((orgId == null) || (days == null)) throw new NullPointerException("extendExpiryDate 参数错误");
        OrgAuthDO entity = orgAuthDAO.selectByPrimaryKey(orgId);
        if (entity == null) throw new IllegalArgumentException ("extendExpiryDate 无法创建OrgAuthDO");

        LocalDateTime expiryDate = (entity.getExpiryDate() != null) ? entity.getExpiryDate().plusDays(days) : null;
        if ((expiryDate == null) && (entity.getApplyDate() != null)) expiryDate = entity.getApplyDate().plusDays(days);
        if ((expiryDate == null) && (entity.getCreateDate() != null)) expiryDate = entity.getCreateDate().plusDays(days);
        if (expiryDate == null) expiryDate = LocalDateTime.now().plusDays(days);
        setExpiryDate(orgId,expiryDate,operatorUserId);
    }

    /**
     * 方法：处理审核
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param authorizeResult 审核结果
     */
    @Override
    public OrgAuthDTO authorizeAuthentication(OrgAuthAuditDTO authorizeResult) {
        final Integer AUTH_PASS_STATUS = 2;
        if ((authorizeResult == null) || (authorizeResult.getId() == null))
            throw new IllegalArgumentException("authorizeAuthentication 参数错误");

        if ((authorizeResult.getAuthenticationStatus() != AUTH_PASS_STATUS) && (authorizeResult.getRejectType() == null)) throw new IllegalArgumentException("不通过审核原因不能为空");

        //保存当次审核结果
        OrgAuthDO origin = orgAuthDAO.selectByPrimaryKey(authorizeResult.getId());
        if (origin == null) throw new IllegalArgumentException("authorizeAuthentication 参数错误");
        OrgAuthDO entity = new OrgAuthDO();
        BeanUtils.copyProperties(origin,entity);
        BeanUtils.copyProperties(authorizeResult,entity);
        if ((origin.getAuthenticationStatus() != AUTH_PASS_STATUS) && (entity.getAuthenticationStatus() == AUTH_PASS_STATUS)){ //首次通过认证
            final Integer EXPIRY_DAYS = 30;
            LocalDateTime expiryDate = (origin.getExpiryDate() != null) ? origin.getExpiryDate().plusDays(EXPIRY_DAYS) : null;
            if ((expiryDate == null) && (origin.getApplyDate() != null)) expiryDate = origin.getApplyDate().plusDays(EXPIRY_DAYS);
            if ((expiryDate == null) && (origin.getCreateDate() != null)) expiryDate = origin.getCreateDate().plusDays(EXPIRY_DAYS);
            if (expiryDate == null) expiryDate = LocalDateTime.now().plusDays(EXPIRY_DAYS);
            entity.setExpiryDate(expiryDate);
        }

        entity.resetUpdateDate();
        orgAuthDAO.updateByPrimaryKey(entity);

        //更新认证信息历史
        orgAuthAuditDAO.updateStatusByOrgId(authorizeResult.getId()); //更新以往数据的isNew字段，设置为0

        OrgAuthAuditDO auditDO = new OrgAuthAuditDO();
        BeanUtils.copyProperties(authorizeResult,auditDO);
        auditDO.setIsNew(1);
        auditDO.setOrgId(authorizeResult.getId());
        auditDO.setApproveDate(entity.getUpdateDate());
        if (authorizeResult.getRejectType() != null) auditDO.setAuditMessage(authorizeResult.getRejectType().toString());
        auditDO.setSubmitDate(entity.getApplyDate());
        auditDO.setCreateDate(entity.getUpdateDate());
        auditDO.setCreateBy(entity.getUpdateBy());
        auditDO.setUpdateBy(null);
        auditDO.setUpdateDate(null);
        auditDO.setAuditPerson(authorizeResult.getAuditorName());
        auditDO.setStatus(entity.getAuthenticationStatus());
        auditDO.resetId();
        orgAuthAuditDAO.insert(auditDO);

        return null; //前端没有使用此信息进行加速，因此不需要返回值 return getAuthenticationById(entity.getId());
    }

    /**
     * 方法：获取注册信息
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param orgId
     */
    @Override
    public OrgAuthDTO getAuthenticationById(String orgId) {
        OrgAuthQueryDTO query = new OrgAuthQueryDTO();
        query.setId(orgId);
        query.setPageSize(1);
        List<OrgAuthDTO> list = listAuthentication(query);
        return (list.size() > 0) ? list.get(0) : null;
    }

    /**
     * 方法：列出申请审核记录
     * 作者：zhangchengliang
     * 日期：2017/7/11
     *
     * @param query 查询过滤条件
     */
    private List<OrgAuthDTO> listAuthentication(OrgAuthQueryDTO query) {
        return listAuthentication(query,null);
    }

    private List<OrgAuthDTO> listAuthentication(OrgAuthQueryDTO query, AtomicInteger total){
        if (query == null) throw new IllegalArgumentException("listAuthentication 参数错误");
        List<OrgAuthDTO> dataList = orgAuthDAO.listOrgAuthenticationInfo(query);
        if (total != null) total.set(commonMapper.getLastQueryCount());
        if ((total != null) && (total.get() > 0)) {
            List<String> idList = new ArrayList<>();
            for (OrgAuthDTO data : dataList) {
                idList.add(data.getId());
            }
            List<OrgAuthAttachDTO> attachList = orgAuthDAO.listOrgAuthAttach(idList);
            for (OrgAuthAttachDTO attach : attachList){
                for (OrgAuthDTO data : dataList){
                    if (StringUtils.isSame(attach.getCompanyId(),data.getId())){
                        if (Objects.equals(attach.getType(), NetFileType.CERTIFICATE_ATTACH)) {
                            data.setSealPhoto(getFilePath(attach));
                        } else if (Objects.equals(attach.getType(), NetFileType.BUSINESS_LICENSE_ATTACH)) {
                            data.setBusinessLicensePhoto(getFilePath(attach));
                        } else if (Objects.equals(attach.getType(), NetFileType.LEGAL_REPRESENTATIVE_ATTACH)){
                            data.setLegalRepresentativePhoto(getFilePath(attach));
                        } else if (Objects.equals(attach.getType(), NetFileType.OPERATOR_ATTACH)) {
                            data.setOperatorPhoto(getFilePath(attach));
                        }                        
                    }
                }
            }
        }
        return dataList;
    }

    @Override
    public OrgAuthPageDTO getAuthenticationPage(OrgAuthQueryDTO query) {
        if (query == null) throw new IllegalArgumentException("getAuthenticationPage 参数错误");
        OrgAuthPageDTO result = new OrgAuthPageDTO();
        AtomicInteger total = new AtomicInteger();
        List<OrgAuthDTO> list = listAuthentication(query,total);
        result.setTotal(total.get());
        if (result.getTotal() > 0) {
            result.setList(list);
        }
        return result;
    }

    private String getFilePath(OrgAuthAttachDTO entity){
        return fastdfsConfig.getWebServerUrl() + entity.getFileGroup() + "/" + entity.getFilePath();
    }
}
