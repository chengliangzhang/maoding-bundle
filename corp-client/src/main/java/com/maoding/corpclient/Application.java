package com.maoding.corpclient;


import com.maoding.core.bean.ApiResult;
import com.maoding.corp.config.CorpClientConfig;
import com.maoding.corp.module.corpclient.service.CoService;
import com.maoding.utils.SpringContextUtils;
import com.maoding.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Wuwq on 2016/12/13.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.maoding.corpclient", "com.maoding.corp.module.corpclient"})
@Import({SpringContextUtils.class, CorpClientConfig.class})
@EnableAspectJAutoProxy(exposeProxy = true)
@Controller
public class Application extends SpringBootServletInitializer {

    /**
     * 是否启用定时任务(调试用）
     **/
    public static Boolean EnableSchedule = true;

    @Autowired
    private CoService coService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        if (StringUtils.isBlank(CorpClientConfig.EndPoint))
            throw new RuntimeException("请先配置EndPoint");
    }

    @RequestMapping(value = "/system/login", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult login(@RequestBody Map<String, Object> param) throws Exception {
        return coService.login(param);
    }

    @RequestMapping(value = "/system/returnNodeStatus", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult returnNodeStatus(@RequestBody Map<String, Object> param) throws Exception {
        return coService.handleMyTaskByProjectNodeId(param);
    }

    /**
     * 根据组织ID获取公司网盘容量信息
     */
    @RequestMapping(value = "/system/getCompanyDiskInfo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult getCompanyDiskInfo(@RequestBody Map<String, Object> param) throws Exception {
        return coService.getCompanyDiskInfo(param);
    }

    /**
     * 根据组织ID更新协同占用空间
     */
    @RequestMapping(value = "/system/updateCorpSizeOnCompanyDisk", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult updateCorpSizeOnCompanyDisk(@RequestBody Map<String, Object> param) throws Exception {
        return coService.updateCorpSizeOnCompanyDisk(param);
    }

    /**
     * 根据Endpoint同步所有内容
     */
    @RequestMapping(value = "/system/syncAllByEndpoint", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult syncAllByEndpoint() throws Exception {
        return coService.syncAllByEndpoint(CorpClientConfig.EndPoint);
    }
}
