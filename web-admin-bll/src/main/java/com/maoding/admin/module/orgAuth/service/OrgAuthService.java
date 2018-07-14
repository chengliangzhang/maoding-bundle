package com.maoding.admin.module.orgAuth.service;

import com.maoding.admin.module.orgAuth.dto.OrgAuthAuditDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthPageDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthQueryDTO;

import java.time.LocalDateTime;

/**
 * Created by Wuwq on 2017/07/11.
 */
public interface OrgAuthService {

    /**
     * 方法：设置免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    void setExpiryDate(String orgId, LocalDateTime expiryDate, String operatorUserId);
    void setExpiryDate(String orgId, LocalDateTime expiryDate);

    /**
     * 方法：延长免费期
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    void extendExpiryDate(String orgId, Integer days, String operatorUserId);
    void extendExpiryDate(String orgId, Integer days);

    /**
     * 方法：处理审核
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    OrgAuthDTO authorizeAuthentication(OrgAuthAuditDTO authorizeResult);

    /**
     * 方法：按页列出申请审核记录
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    OrgAuthPageDTO getAuthenticationPage(OrgAuthQueryDTO query);

    /**
     * 方法：获取注册信息
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    OrgAuthDTO getAuthenticationById(String orgId);
}
