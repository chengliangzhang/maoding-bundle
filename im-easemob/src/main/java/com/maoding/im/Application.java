package com.maoding.im;

import com.maoding.utils.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Wuwq on 2016/12/13.
 */
@Controller
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.maoding.im"})
@Import({SpringContextUtils.class})
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableJms
public class Application {

    /**
     * 是否启用定时任务(调试用）
     **/
    public static Boolean EnableSchedule = true;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    public String home() throws Exception{
        return "启动成功";
    }
}
