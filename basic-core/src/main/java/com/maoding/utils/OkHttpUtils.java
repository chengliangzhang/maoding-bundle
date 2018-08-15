package com.maoding.utils;

/**
 * Created by Wuwq on 2017/1/3.
 */

import com.maoding.core.bean.ApiResult;
import okhttp3.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.OkHttpClientHttpRequestFactory;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils extends OkHttpClientHttpRequestFactory {
    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(OkHttpUtils.class);

    private static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";
    private static final String MEDIA_TYPE_FILE = "application/octet-stream; charset=utf-8";
    private static final String MEDIA_TYPE_DEFAULT = MEDIA_TYPE_JSON;

    private static final int CONNECTION_TIME_OUT = 20;
    private static final int READ_TIME_OUT = 30;
    private static final int WRITE_TIME_OUT = 30;

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build();

    private static final String CHARSET_NAME = "UTF-8";

    private static OkHttpClient getHttpClient(){
        if (httpClient == null){
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }

    /**
     * 同步请求
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

    /**
     * 异步请求（带回调）
     */
    public static void enqueue(Request request, Callback responseCallback) {
        httpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 异步请求（不在意返回结果，实现空callback)
     */
    public static void enqueue(Request request) {
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@Nonnull Call call, @Nonnull IOException e) {

            }

            @Override
            public void onResponse(@Nonnull Call call, @Nonnull Response response) throws IOException {

            }
        });
    }


    /**
     * 同步PostJson
     * @throws Exception
     */
    public static Response postJson(String url, Object param) throws Exception {
        String json = JsonUtils.obj2json(param);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        return execute(request);
    }

    /**
     * 异步PostJson
     * @throws Exception
     */
    public static void postJsonAsync(String url, Object param, Callback responseCallback) throws Exception {
        String json = JsonUtils.obj2json(param);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        if (responseCallback == null)
            enqueue(request);
        else
            enqueue(request, responseCallback);
    }

    /**
     * 同步获取Response
     * @throws IOException
     */
    public static Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    /**
     * 异步获取Response
     * @throws IOException
     */
    public static void getAsync(String url, Callback responseCallback) throws IOException {
        Request request = new Request.Builder().url(url).build();
        if (responseCallback == null)
            enqueue(request);
        else
            enqueue(request, responseCallback);
    }

    /**
     * 这里使用了HttpClinet的API
     */
    public static String formatParams(List<BasicNameValuePair> params) {
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }

    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数
     */
    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params) {
        return url + "?" + formatParams(params);
    }

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数
     */
    public static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }

    /**
     * 取消指定请求
     */
    public void cancelRequest(Object tag) {
        for (Call call : httpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : httpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelAllRequest() {
        for (Call call : httpClient.dispatcher().queuedCalls())
            call.cancel();

        for (Call call : httpClient.dispatcher().runningCalls())
            call.cancel();
    }

    /**
     * 描述     默认的响应处理
     * 日期     2018/8/14
     * @author  张成亮
     * @return  响应处理对象
     **/
    public static Callback getDefaultCallback(){
        return new Callback() {
            @Override
            public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
                TraceUtils.enter(log,call,e);
            }

            @Override
            public void onResponse(@Nonnull Call call, @Nonnull Response response) throws IOException {
                TraceUtils.enter(log,call,response);
            }
        };
    }

    /**
     * 描述     使用post连接发送数据并等待返回
     * 日期     2018/8/9
     * @author  张成亮
     * @return  服务返回信息，如果调用错误产生UnsupportedOperationException异常
     * @param   url 服务地址
     * @param   data 调用参数
     **/
    public static <T> Object postData(@NotNull String url, T data) {
        //创建申请对象
        Request request = createPostRequest(url,data);
        //同步调用
        Object result;
        try {
            //发出申请
            Response response = getHttpClient().newCall(request)
                    .execute();

            //获取返回值
            result = getData(response);
        } catch (IOException e) {
            String logMessage = getErrorMessage(url,true);
            String showMessage = getErrorMessage(url,false);

            if (StringUtils.isNotEmpty(e.getMessage())) {
                logMessage += ":" + e.getMessage();
                showMessage += ":" + e.getMessage();
            }
            log.error(logMessage);
            throw new UnsupportedOperationException(showMessage);
        }

        return result;
    }

    //从返回值内解析出实际包含的数据,如果包含错误则产生IOException异常
    private static Object getData(Response response) throws IOException {
        //获取返回值
        if (response == null || !response.isSuccessful()){
            throw new IOException();
        }

        //获取返回值内的实际对象
        Object result = null;
        ResponseBody body = response.body();
        if (body != null){
            ApiResult<?> apiResult = null;
            try {
                apiResult = BeanUtils.createFromJson(body.string(), ApiResult.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //如果调用结果正常，返回结果，否则报告出现异常
            if (apiResult != null) {
                if (apiResult.isSuccessful()) {
                    result = apiResult.getData();
                } else {
                    throw new IOException(apiResult.getMsg());
                }
            }
        }

        return result;
    }

    //获取错误字符串
    private static String getErrorMessage(@NotNull String url,boolean isAddCaller){
        String msg = "访问" + url + "时出现错误";
        if (isAddCaller){
            msg += ":" + getCaller();
        }
        return msg;
    }

    //获取调用者位置字符串
    private static String getCaller(){
        return StringUtils.getCaller(OkHttpUtils.class.getName());
    }

    /**
     * 描述     使用post连接发送数据,不等待返回数据
     * 日期     2018/8/9
     * @author  张成亮
     * @param   url 服务地址
     * @param   data 调用参数
     **/
    public static <T> void postDataAsyn(@NotNull String url,T data,Callback callback) {
        //创建申请对象
        Request request = createPostRequest(url,data);
        //异步调用
        getHttpClient().newCall(request)
                .enqueue((callback != null) ? callback : getDefaultCallback());
    }

    //创建发送JSON申请对象
    private static <T> Request createPostRequest(@NotNull String url, T data){
        String json = StringUtils.toJsonString(data);
        MediaType mediaType = MediaType.parse(MEDIA_TYPE_DEFAULT);
        RequestBody body = RequestBody.create(mediaType, json);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }
}