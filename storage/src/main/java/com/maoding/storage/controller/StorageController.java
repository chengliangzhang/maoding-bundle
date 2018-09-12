package com.maoding.storage.controller;

import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import com.maoding.storage.dto.FdNodeDTO;
import com.maoding.storage.dto.FdNodeQueryDTO;
import com.maoding.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/12
 * 类名: com.maoding.storage.controller.StorageController
 * 作者: 张成亮
 * 描述:
 **/
@Controller
public class StorageController extends BaseController {
    @Autowired
    private StorageService storageService;

    /**
     * 描述       查询文件列表
     * 日期       2018/9/12
     * @author   张成亮
     **/
    @RequestMapping(value = "/listFdNode", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult listFdNode(@RequestBody FdNodeQueryDTO query) {
        updateCurrentUserInfo(query);
        List<FdNodeDTO> list = storageService.listFdNode(query);
        return ApiResult.success(list);
    }

}
