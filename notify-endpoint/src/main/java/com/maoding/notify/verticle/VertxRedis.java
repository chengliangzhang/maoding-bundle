/*
package com.maoding.notify.endpoint.verticle;


import com.maoding.notify.endpoint.config.AppConfig;
import com.maoding.notify.endpoint.core.SpringContextUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

*/
/**
 * Created by Idccapp21 on 2016/10/12.
 *//*

public class VertxRedis extends AbstractVerticle {

    private AppConfig appConfig;

    public VertxRedis(){
        appConfig= SpringContextUtils.getApplicationContext().getBean(AppConfig.class);
    }

    @Override
    public void start() throws Exception {
        RedisOptions config = new RedisOptions()
                .setHost("172.16.6.73").setPort(6479).setAuth("123456").setTcpKeepAlive(true);

        vertx.eventBus().<JsonObject>consumer("io.vertx.redis.woshi", received -> {
            // do whatever you need to do with your message
            JsonObject value = received.body().getJsonObject("value");
            System.out.println(value);
        });
        RedisClient redis = RedisClient.create(vertx, config);

        redis.subscribe("woshi",res->{
            if(res.succeeded()){
                System.out.println(res.result());
            }
        });
    }
}
*/
