package com.maoding.corpclient.config;

import com.maoding.utils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

/**
 * Created by Wuwq on 2017/2/6.
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private Environment env;

    @Value("${redisson.address}")
    private String address;

    @Value("${redisson.password}")
    private String password;

    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        Config config = new Config();
        SingleServerConfig ssc = config.useSingleServer();
        ssc.setAddress(address);
        if (!StringUtils.isBlank(password))
            ssc.setPassword(password);
        RedissonClient client = Redisson.create(config);
        return client;
    }
}
