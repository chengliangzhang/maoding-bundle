package com.maoding.corp.module.corpclient.service;

import com.maoding.constDefine.corp.CorpServer;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Wuwq on 2017/4/24.
 */
@Service("coService")
public class CoServiceImpl extends BaseService implements CoService {

    private static final Logger log = LoggerFactory.getLogger(CoServiceImpl.class);

    @Autowired
    private SyncService syncService;

    /**
     * 登录接口
     */
    @Override
    public ApiResult login(Map<String, Object> param) throws Exception {
        ApiResult apiResult = syncService.pullFromCorpServer(param, CorpServer.URL_LOGIN);
        if (apiResult.isSuccessful()) {
            return apiResult;
        }
        return ApiResult.failed(null, null);
    }

    /**
     * 根据组织ID获取公司网盘容量信息
     */
    @Override
    public ApiResult getCompanyDiskInfo(Map<String, Object> param) throws Exception {
        String companyId = (String) param.get("companyId");
        log.info("组织 {} 请求CorpServer获取网盘信息", companyId);
        ApiResult apiResult = syncService.pullFromCorpServer(param, CorpServer.URL_GET_COMPANY_DISK_INFO);
        return apiResult;
    }

    /**
     * 根据组织ID更新协同占用空间
     */
    @Override
    public ApiResult updateCorpSizeOnCompanyDisk(Map<String, Object> param) throws Exception {
        String companyId = (String) param.get("companyId");
        Long corpSize = Long.parseLong((String) param.get("corpSize"));
        log.info("组织 {} 请求CorpServer更新协同占用空间：{}({})", companyId, corpSize, StringUtils.getSize(corpSize));
        ApiResult apiResult = syncService.pullFromCorpServer(param, CorpServer.URL_GET_UPDATE_CORP_SIZE);
        return apiResult;
    }

    /**
     * 设置节点完成状态
     */
    @Override
    public ApiResult handleMyTaskByProjectNodeId(Map<String, Object> param) throws Exception {
        ApiResult apiResult = syncService.pullFromCorpServer(param, CorpServer.URL_HANDLE_MY_TASK_BY_PROJECT_NODE_ID);
        return apiResult;
    }

    /**
     * 同步端点下所有内容
     */
    @Override
    public ApiResult syncAllByEndpoint(String endpoint) throws Exception {
        ApiResult apiResult = syncService.pullFromCorpServer(CorpServer.URL_SYNC_ALL_BY_ENDPOINT + "/" + endpoint);
        return apiResult;
    }
}

