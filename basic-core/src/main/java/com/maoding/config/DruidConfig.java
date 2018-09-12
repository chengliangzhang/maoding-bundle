package com.maoding.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.maoding.utils.TraceUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

public class DruidConfig {
    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired
    protected MybatisProperties mybatisProperties;

    @Bean(name = "dataSource")
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            //除复制属性外还需要调用相应方法，此处不能使用自定义的快速的copyProperties
            org.springframework.beans.BeanUtils.copyProperties(this, dataSource);
        } catch (BeansException e) {
            TraceUtils.error("创建dataSource时出错");
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
