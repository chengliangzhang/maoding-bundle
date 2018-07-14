package com.maoding.notify;


import com.maoding.notify.module.schedule.service.ScheduleService;
import com.maoding.notify.verticle.SockJsServerVerticle;
import com.maoding.utils.SpringContextUtils;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

/**
 * Created by Wuwq on 2016/10/10.
 */
@Controller
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.maoding.notify"})
@EnableAspectJAutoProxy(exposeProxy = true)
@Import({SpringContextUtils.class})
@EnableJms
public class Application {

    @Autowired
    private SockJsServerVerticle sockJsServerVerticle;

    @Autowired
    private SpringContextUtils springContextUtils;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

       /* Vertx vertx =Vertx.vertx() ;
        VertxUtils.setVertx(vertx);
        //vertx.deployVerticle(SpringContextUtils.getApplicationContext().getBean(HttpServerVerticle.class));
        vertx.deployVerticle(SpringContextUtils.getApplicationContext().getBean(SockJsServerVerticle.class));*/
    }

    @PostConstruct
    public void deployVerticle() {
        Vertx.vertx().deployVerticle(sockJsServerVerticle);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() throws Exception{
        return "启动成功";
    }
}
