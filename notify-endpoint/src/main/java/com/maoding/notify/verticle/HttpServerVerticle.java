package com.maoding.notify.verticle;

import com.maoding.notify.config.AppConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Wuwq on 2016/10/10.
 */

@Component
public class HttpServerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CorsHandler corsHandler;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        HttpServerOptions opts = new HttpServerOptions();
        opts.setHost(appConfig.getHttpHost());
        opts.setPort(appConfig.getHttpPort());

        //处理跨域问题
        router.route().handler(corsHandler);

        router.route().handler(StaticHandler.create("webroot").setCachingEnabled(false));

        vertx.createHttpServer(opts).requestHandler(router::accept).listen();
    }
}
