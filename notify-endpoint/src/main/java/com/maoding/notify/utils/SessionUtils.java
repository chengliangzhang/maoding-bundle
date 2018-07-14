package com.maoding.notify.utils;

import io.vertx.ext.web.handler.sockjs.SockJSSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wuwq on 2016/10/12.
 */
public class SessionUtils {
    public final static String Key_UserId="userId";
    public final static String Key_UserToken="userToken";

    private static final Map<String, SockJSSocket> sessions = new HashMap<>();

    public static void add(String userId, SockJSSocket session) {
        sessions.put(userId, session);
    }

    public static SockJSSocket remove(String userId) {
        return sessions.remove(userId);
    }

    public static List<String> getAllUserIds() {
        return new ArrayList<>(sessions.keySet());
    }

    public static SockJSSocket get(String userId) {
        return sessions.get(userId);
    }

    public static boolean exists(String userId) {
        return get(userId) != null;
    }
}
