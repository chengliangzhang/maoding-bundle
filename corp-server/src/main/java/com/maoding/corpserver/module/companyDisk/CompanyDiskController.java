package com.maoding.corpserver.module.companyDisk;

import com.maoding.common.module.companyDisk.dto.CorpDeployTypeDTO;
import com.maoding.common.module.companyDisk.dto.RecalcSizeDTO;
import com.maoding.common.module.companyDisk.service.CompanyDiskService;
import com.maoding.constDefine.companyDisk.FileSizeSumType;
import com.maoding.core.bean.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Wuwq on 2017/06/07.
 */
@RestController
@RequestMapping("/corpserver/companyDisk")
public class CompanyDiskController {

    @Autowired
    private CompanyDiskService companyDiskService;

    /**
     * 根据组织ID重新统计指定类型文件大小
     */
    @RequestMapping(value = "/recalcSizeByCompanyId", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult recalcSizeByCompanyId(@RequestBody RecalcSizeDTO dto) {
        if (dto.getSumType() == null)
            return companyDiskService.recalcSize(dto.getCompanyId(), null);
        return companyDiskService.recalcSize(dto.getCompanyId(), FileSizeSumType.valueOf(dto.getSumType()));
    }


    /**
     * 根据组织ID切换协同部署方式
     */
    @RequestMapping(value = "/switchCorpDeployType", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult switchCorpDeployType(@RequestBody CorpDeployTypeDTO dto) {
        return companyDiskService.switchCorpDeployType(dto.getCompanyId(), dto.getCorpOnCloud());
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

    /**
     * 根据组织ID更新协同占用空间
     */
    @RequestMapping(value = "/updateCorpSizeOnCompanyDisk", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateCorpSizeOnCompanyDisk(@RequestBody Map<String, Object> param) {
        String companyId = (String) param.get("companyId");
        Long corpSize = Long.parseLong((String) param.get("corpSize"));
        return companyDiskService.updateCorpSize(companyId, corpSize);
    }
}
