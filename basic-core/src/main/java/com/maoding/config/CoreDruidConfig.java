package com.maoding.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

public class CoreDruidConfig {
    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired
    protected MybatisProperties mybatisProperties;

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            BeanUtils.copyProperties(this, dataSource);
        } catch (Exception e) {
            throw new RuntimeException("failed to init datasource", e);
        }
        return dataSource;
    }


    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    @Autowired
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(mybatisProperties.getConfigLocation())) {
            factory.setConfigLocation(this.resourceLoader.getResource(mybatisProperties.getConfigLocation()));
        }
        org.apache.ibatis.session.Configuration configuration = mybatisProperties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(mybatisProperties.getConfigLocation())) {
            configuration = new org.apache.ibatis.session.Configuration();
        }
        factory.setConfiguration(configuration);
        if (mybatisProperties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(mybatisProperties.getConfigurationProperties());
        }
        if (StringUtils.hasLength(mybatisProperties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(mybatisProperties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(mybatisProperties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(mybatisProperties.resolveMapperLocations())) {
            factory.setMapperLocations(mybatisProperties.resolveMapperLocations());
        }

        return factory.getObject();
    }

}
