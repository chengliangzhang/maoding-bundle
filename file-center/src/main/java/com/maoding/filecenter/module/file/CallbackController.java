package com.maoding.filecenter.module.file;

import com.maoding.core.bean.ApiResult;
import com.maoding.filecenter.module.file.dto.CallbackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


/**
 * Created by sandy on 2017/11/1.
 */
@RestController
@RequestMapping("/fileCenter/callback")
public class CallbackController {

    Logger logger = LoggerFactory.getLogger(CallbackController.class);

    /**
     * 上传项目合同扫描件
     */
    @RequestMapping(value = "/callbackNotParam", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult callbackNotParam() throws Exception {
        return ApiResult.success("请求成功","测试数据");
    }


    /**
     * 上传项目合同扫描件
     */
    @RequestMapping(value = "/callbackParam", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult callbackParam(@RequestBody CallbackDTO callback) throws Exception {
        logger.info("cellphone==="+callback.getCellphone()+"  password=="+callback.getPassword());
        logger.error("bucket==="+callback.getBucket()+"  size=="+callback.getSize());
        return ApiResult.success("请求成功",callback);
    }
}
