/*
package com.maoding.notify.endpoint.config;

import io.vertx.redis.RedisOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

*/
/**
 * Created by Wuwq on 2016/10/11.
 *//*

@Configuration
@PropertySource(value = {"classpath:properties/redis.properties"})
public class RedisConfig {
    @Value("${redis.host}")
    public String Host;

    @Value("${redis.port}")
    public int Port;

    //@Value("${redis.timeout}")
    //public int Timeout;

    @Value("${redis.password}")
    public String Password;

    @Bean
    public RedisOptions getRedisOptions(){
        return new RedisOptions().setHost(Host).setPort(Port).setAuth(Password).setTcpKeepAlive(true);
    }
}
*/
