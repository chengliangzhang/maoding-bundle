package com.maoding.corpserver.module.syncCompany;

import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import com.maoding.corp.module.corpserver.dto.SyncCompanyDTO_Create;
import com.maoding.corp.module.corpserver.dto.SyncCompanyDTO_Update;
import com.maoding.corp.module.corpserver.service.CollaborationService;
import com.maoding.corp.module.corpserver.service.SyncCompanyServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Wuwq on 2017/2/10.
 */
@RestController
@RequestMapping("/corpserver/syncCompany")
public class SyncCompanyController extends BaseController {

    @Autowired
    private SyncCompanyServise syncCompanyServise;

    @Autowired
    private CollaborationService collaborationService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult create(@RequestBody SyncCompanyDTO_Create dto) throws Exception {
        return syncCompanyServise.create(dto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult update(@RequestBody SyncCompanyDTO_Update dto) throws Exception {
        return syncCompanyServise.update(dto);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult delete(String corpEndpoint, String companyId) throws Exception {
        return syncCompanyServise.delete(corpEndpoint, companyId);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult delete(@PathVariable String id) throws Exception {
        return syncCompanyServise.delete(id);
    }

    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult selectAll() throws Exception {
        return syncCompanyServise.select(null);
    }

    @RequestMapping(value = "/select/{corpEndpoint}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult select(@PathVariable String corpEndpoint) throws Exception {
        return syncCompanyServise.select(corpEndpoint);
    }

    /**
     * 同步组织到Redis
     */
    @RequestMapping(value = "/syncToRedis", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult syncToRedis() throws Exception {
        return syncCompanyServise.syncToRedis();
    }

    /**
     * 根据SyncCompanyId同步所有内容
     */
    @RequestMapping(value = "/pushSyncAllCmd/{syncCompanyId}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult pushSyncAllCmd(@PathVariable String syncCompanyId) throws Exception {
        collaborationService.pushSyncCMD_SyncAllByCompany(syncCompanyId);
        return ApiResult.success(null, null);
    }
}
