package com.maoding.filecenter;

import com.maoding.config.MultipartConfig;
import com.maoding.fastdfsClient.FdfsClientConfig;
import com.maoding.utils.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Wuwq on 2016/12/13.
 */
@Controller
@Configuration
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@ComponentScan(basePackages = {"com.maoding.filecenter","com.maoding.common"})
@Import({SpringContextUtils.class, MultipartConfig.class, FdfsClientConfig.class})
@EnableAspectJAutoProxy(exposeProxy = true)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() throws Exception{
        return "启动成功";
    }
}
