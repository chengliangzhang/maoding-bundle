package com.maoding.admin.module.historyData;

import com.maoding.admin.module.historyData.dto.ImportResultDTO;
import com.maoding.admin.module.historyData.service.ImportService;
import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/7/19.
 */
@Controller
@RequestMapping("/historyData")
public class HistoryDataController extends BaseController {

    @Autowired
    ImportService importService;

    @RequestMapping("/entry")
    public void entry() {

    }

    @RequestMapping(value = "/importProjects", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult importProjects(MultipartFile file) throws Exception {
        InputStream in = file.getInputStream();
        String token = null; //todo 用户标识参数
        ImportResultDTO result = importService.importProjects(in,token); //读入文件
        return ApiResult.success(result);
    }

    @RequestMapping(value = "/createProjects", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult importProjects(List<Map<String,Object>> dataList) throws Exception {
        String token = null; //todo 用户标识参数
        ImportResultDTO result = importService.importProjects(dataList, token); //生成数据库记录
        return ApiResult.success(result);
    }
}
