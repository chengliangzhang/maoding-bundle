package com.maoding.im.easemob.comm;

import com.google.gson.Gson;
import com.maoding.core.bean.ApiResult;
import io.swagger.client.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wuwq on 2016/10/13.
 */
public class ApiResultHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiResultHandler.class);

    public ApiResult handle(EasemobAPI easemobAPI) {
        Object resObj = null;
        try {
            resObj = easemobAPI.invokeEasemobAPI();
        } catch (ApiException e) {
            if (e.getCode() == 403) {
                //（禁止）服务器拒绝请求。对于群组/聊天室服务，表示本次调用不符合群组/聊天室操作的正确逻辑，例如调用添加成员接口，添加已经在群组里的用户，或者移除聊天室中不存在的成员等操作。
                return ApiResult.success(resObj);
            }

            if (e.getCode() == 401) {
                logger.info("当前Token无效，正在生成新Token进行重连");
                TokenUtils.initTokenByProp();
                try {
                    resObj = easemobAPI.invokeEasemobAPI();
                    return resObj != null ? ApiResult.success(resObj) : ApiResult.failed(null);
                } catch (ApiException e1) {
                    String msg = getApiExceptionDetails(e1);
                    logger.error(msg);
                    return ApiResult.failed(msg);
                }
            } else if (e.getCode() == 429) {
                logger.warn("请求过于频繁");
                return ApiResult.failed("请求过于频繁");
            } else if (e.getCode() >= 500) {
                return retry(easemobAPI);
            }
            String msg = getApiExceptionDetails(e);
            logger.error(msg);
            return ApiResult.failed(msg);
        }
        return resObj != null ? ApiResult.success(resObj) : ApiResult.failed(null);
    }

    private ApiResult retry(EasemobAPI easemobAPI) {
        Object resObj;
        long time = 5;
        for (int i = 0; i < 3; i++) {
            try {
                TimeUnit.SECONDS.sleep(time);
                logger.info("服务器连接异常，开始重试第 {} 次", i + 1);
                resObj = easemobAPI.invokeEasemobAPI();
                if (resObj != null)
                    return ApiResult.success(resObj);
            } catch (ApiException e) {
                logger.error(getApiExceptionDetails(e));
                time *= 3;
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        logger.error("重试多次均失败");
        return ApiResult.failed("重试多次均失败");
    }

    private String getApiExceptionDetails(ApiException e) {
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(e.getResponseBody(), Map.class);
        if (map == null)
            return String.format("error_code:%s error_msg:%s", e.getCode(), e.getMessage());
        return String.format("error_code:%s error_msg:%s error_desc:%s", e.getCode(), e.getMessage(), map.get("error_description"));
    }
}
