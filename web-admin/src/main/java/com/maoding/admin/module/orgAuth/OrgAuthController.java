package com.maoding.admin.module.orgAuth;

import com.maoding.admin.module.orgAuth.dto.OrgAuthAuditDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthPageDTO;
import com.maoding.admin.module.orgAuth.dto.OrgAuthQueryDTO;
import com.maoding.admin.module.orgAuth.service.OrgAuthService;
import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Wuwq on 2017/07/11.
 */
@Controller
@RequestMapping("/orgAuth")
public class OrgAuthController extends BaseController {

    @Autowired
    OrgAuthService orgAuthService;

    @RequestMapping("/approveList")
    public void approveList() {
    }

    /**
     * 方法：进行审核
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    @RequestMapping(value = "/authorizeAuthentication", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult authorizeAuthentication(@RequestBody OrgAuthAuditDTO authorizeResult) throws Exception {
        OrgAuthDTO result = orgAuthService.authorizeAuthentication(authorizeResult);
        return ApiResult.success(result);
    }

    /**
     * 方法：按页列出申请审核记录
     * 作者：zhangchengliang
     * 日期：2017/7/11
     */
    @RequestMapping(value = "/getAuthenticationPage", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult getAuthenticationPage(@RequestBody OrgAuthQueryDTO query) throws Exception {
        OrgAuthPageDTO result = orgAuthService.getAuthenticationPage(query);
        return ApiResult.success(result);
    }
}
