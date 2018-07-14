package com.maoding.yongyoucloud.module.enterprise.controller;

import com.maoding.core.bean.ApiResult;
import com.maoding.utils.StringUtils;
import com.maoding.yongyoucloud.module.enterprise.dto.OperateLinkmanDTO;
import com.maoding.yongyoucloud.module.enterprise.service.EnterpriseLinkmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 企业公商信息查询
 */
@Controller
@RequestMapping("/yongyoucloud/enterpriseLinkman")
public class EnterpriseLinkmanController {

    @Autowired
    private EnterpriseLinkmanService enterpriseLinkmanService;

    /**
     * 保存联系人
     */
    @RequestMapping("/saveLinkman")
    @ResponseBody
    public ApiResult saveLinkman(@RequestBody OperateLinkmanDTO dto) throws Exception{
        if(StringUtils.isNullOrEmpty(dto.getEnterpriseOrgId())){
            return ApiResult.failed("参数错误");
        }
        return enterpriseLinkmanService.saveLinkman(dto);
    }

    /**
     * 删除联系人
     */
    @RequestMapping("/deleteLinkman")
    @ResponseBody
    public ApiResult deleteLinkman(@RequestBody OperateLinkmanDTO dto) throws Exception{
        if(StringUtils.isNullOrEmpty(dto.getId()) || StringUtils.isNullOrEmpty(dto.getProjectLinkmanId())){
            return ApiResult.failed("参数错误");
        }
        return enterpriseLinkmanService.deleteLinkman(dto);
    }

}
