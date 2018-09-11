package com.maoding.storage;

import com.maoding.utils.DigitUtils;
import com.maoding.utils.TraceUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/11
 * 类名: com.maoding.storage.Application
 * 作者: 张成亮
 * 描述: 存储管理模块
 **/
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
@ComponentScan(basePackages = {"com.maoding"})
@ConfigurationProperties(prefix = "startup")
public class Application extends SpringApplication implements SchedulingConfigurer {

    private static boolean started = false;

    /** 定时任务池大小 **/
    private Integer schedulePoolSize;

    /** 是否定时清理文件 **/
    private Integer isAutoClear;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        started = true;
    }

    @Scheduled(fixedDelay  = 5 * 1000)
    public void schedule() throws Exception {
        //如果尚未完全启动，不执行定义任务
        if (!started) {
            return;
        }

        //定期清理服务器文件
        if (DigitUtils.parseBoolean(getIsAutoClear())) {
            long t = TraceUtils.info("------- ------- ------- 清理文件 ------- ------- -------");
            TraceUtils.info("------- ------- 结束清理文件，用时" + (System.currentTimeMillis() - t) + "ms ------- -------");
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(
                (getSchedulePoolSize() == null) ? 1 : getSchedulePoolSize());
    }

    public Integer getSchedulePoolSize() {
        return schedulePoolSize;
    }

    public void setSchedulePoolSize(Integer schedulePoolSize) {
        this.schedulePoolSize = schedulePoolSize;
    }

    public Integer getIsAutoClear() {
        return isAutoClear;
    }

    public void setIsAutoClear(Integer isAutoClear) {
        this.isAutoClear = isAutoClear;
    }
}
