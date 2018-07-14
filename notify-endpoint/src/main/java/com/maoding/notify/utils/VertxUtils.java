package com.maoding.notify.utils;

import io.vertx.core.Vertx;

/**
 * Created by Wuwq on 2016/10/12.
 */
public class VertxUtils {
    private static Vertx vertx;

    public static Vertx getVertx() {
        return vertx;
    }

    public static void setVertx(Vertx vertx) {
        VertxUtils.vertx = vertx;
    }
}
