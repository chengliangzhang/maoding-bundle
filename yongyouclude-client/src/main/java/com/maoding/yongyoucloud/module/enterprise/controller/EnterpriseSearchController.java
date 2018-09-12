package com.maoding.yongyoucloud.module.enterprise.controller;

import com.maoding.core.bean.ApiResult;
import com.maoding.utils.StringUtils;
import com.maoding.yongyoucloud.module.enterprise.dto.EnterpriseSearchQueryDTO;
import com.maoding.yongyoucloud.module.enterprise.service.EnterpriseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 企业公商信息查询
 */
@Controller
@RequestMapping("/yongyoucloud/enterpriseSearch")
public class EnterpriseSearchController {

    @Autowired
    private EnterpriseSearchService enterpriseSearchService;

    /**
     * 根据id或许name查询（id，name必须存在其中一个）
     */
    @RequestMapping("/enterpriseStockHolder")
    @ResponseBody
    public ApiResult enterpriseStockHolder(@RequestBody EnterpriseSearchQueryDTO dto) throws Exception{
        if(StringUtils.isNullOrEmpty(dto.getEnterpriseId()) && StringUtils.isNullOrEmpty(dto.getName())){
            return ApiResult.failed("查询参数错误");
        }
        return ApiResult.success(enterpriseSearchService.enterpriseStockHolder(dto));
    }

    /**
     * 模糊查询列表，更据dto.name
     */
    @RequestMapping("/queryAutoComplete")
    @ResponseBody
    public ApiResult queryAutoComplete(@RequestBody EnterpriseSearchQueryDTO dto) throws Exception{
        if(StringUtils.isNullOrEmpty(dto.getName())){
            return ApiResult.failed("查询参数错误");
        }
        return ApiResult.success(enterpriseSearchService.queryAutoComplete(dto));
    }

    /**
     * 根据id查询详细信息
     */
    @RequestMapping("/queryDetail")
    @ResponseBody
    public ApiResult queryDetail(@RequestBody EnterpriseSearchQueryDTO dto) throws Exception{
        try{
            if(StringUtils.isNullOrEmpty(dto.getEnterpriseId())){
                return ApiResult.failed("查询参数错误");
            }
            return ApiResult.success(enterpriseSearchService.queryDetail(dto));
        }catch (Exception e){
            e.printStackTrace();
        }
        return ApiResult.failed("查询参数错误");
    }

    /**
     * 根据id查询详细信息
     */
    @RequestMapping("/queryEnterpriseDetail")
    @ResponseBody
    public ApiResult queryEnterpriseDetail(@RequestBody EnterpriseSearchQueryDTO dto) throws Exception{
        try{
            if(StringUtils.isNullOrEmpty(dto.getEnterpriseOrgId())){
                return ApiResult.failed("查询参数错误");
            }
            return ApiResult.success(enterpriseSearchService.queryEnterpriseDetail(dto));
        }catch (Exception e){
            e.printStackTrace();
        }
        return ApiResult.failed("查询参数错误");
    }


    /**
     * 根据企业全称查询详细信息 dto.name
     */
    @RequestMapping("/queryFull")
    @ResponseBody
    public ApiResult queryFull(@RequestBody EnterpriseSearchQueryDTO dto) throws Exception{
        if(StringUtils.isNullOrEmpty(dto.getName())){
            return ApiResult.failed("查询参数错误");
        }
        return ApiResult.success(enterpriseSearchService.queryFull(dto));
    }


    /**
     * 查询当前公司的客户列表
     */
    @RequestMapping("/getEnterprise")
    @ResponseBody
    public ApiResult getEnterprise(@RequestBody EnterpriseSearchQueryDTO dto) throws Exception{
        if(StringUtils.isNullOrEmpty(dto.getCompanyId())){
            return ApiResult.failed("查询参数错误");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("enterpriseList",enterpriseSearchService.getEnterprise(dto));
        return ApiResult.success(data);
    }
}
