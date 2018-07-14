package com.maoding.utils;

/**
 * Created by Wuwq on 2017/1/3.
 */

import okhttp3.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    private static final int CONNECTION_TIME_OUT = 20;
    private static final int READ_TIME_OUT = 30;
    private static final int WRITE_TIME_OUT = 30;

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build();

    private static final String CHARSET_NAME = "UTF-8";

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
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

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
}