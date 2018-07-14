package com.maoding.corpserver;

import com.maoding.utils.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Wuwq on 2016/12/13.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.maoding.corpserver", "com.maoding.corp.module.corpserver", "com.maoding.common"})
@Import({SpringContextUtils.class})
@EnableAspectJAutoProxy(exposeProxy = true)
@Controller
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/corpserver/setting")
    public String setting() {
        return "corpserver/setting";
    }
}
