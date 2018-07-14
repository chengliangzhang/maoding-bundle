package com.maoding.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Wuwq on 2017/5/6.
 */
public class GsonUtils {
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public static GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public static String toJson(Object obj) {
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(obj);
        return json;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = gsonBuilder.create();
        T obj = gson.fromJson(json, classOfT);
        return obj;
    }
}
