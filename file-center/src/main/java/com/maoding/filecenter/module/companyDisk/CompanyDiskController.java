package com.maoding.filecenter.module.companyDisk;

import com.maoding.common.module.companyDisk.service.CompanyDiskService;
import com.maoding.core.bean.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Wuwq on 2017/05/26.
 */
@Controller
@RequestMapping("/fileCenter/companyDisk")
public class CompanyDiskController {

    @Autowired
    private CompanyDiskService companyDiskService;

    @RequestMapping("/initAllDisk")
    @ResponseBody
    public ApiResult initAllDisk() {
        return companyDiskService.initAllDisk();
    }

    @RequestMapping("/initDisk/{companyId}")
    @ResponseBody
    public ApiResult initDisk(@PathVariable String companyId) {
        return companyDiskService.initDisk(companyId);
    }

    /**
     * 根据组织ID获取公司网盘容量信息
     */
    @RequestMapping(value = "/getCompanyDiskInfo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult getCompanyDiskInfo(@RequestBody Map<String, Object> param) {
        String companyId = (String) param.get("companyId");
        return companyDiskService.getCompanyDiskInfo(companyId);
    }
}
