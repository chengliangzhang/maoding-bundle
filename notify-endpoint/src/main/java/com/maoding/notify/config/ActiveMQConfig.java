package com.maoding.notify.config;

import com.maoding.notify.module.message.dto.SendMessageDataDTO;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuwq on 2016/10/13.
 */
@Configuration
public class ActiveMQConfig {
    public final static String LISTENER_CONTAINER_QUEUE = "LISTENER_CONTAINER_QUEUE";
    public final static String LISTENER_CONTAINER_TOPIC = "LISTENER_CONTAINER_TOPIC";

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String user;
    @Value("${spring.activemq.password}")
    private String password;

    /**
     * 创建 ActiveMQ 的连接工厂
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(user);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * JMS 队列的监听容器工厂
     */
    @Bean(name = LISTENER_CONTAINER_QUEUE)
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置连接数
        //factory.setConcurrency("3-10");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /*@Bean(name = LISTENER_CONTAINER_TOPIC)
    public JmsListenerContainerFactory<?> jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //设置连接数
        //factory.setConcurrency("1");
        factory.setPubSubDomain(true);
        configurer.configure(factory, connectionFactory);
        return factory;
    }*/

    /**
     * JMS 队列的模板类
     */
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        JmsTemplate template = new JmsTemplate();
        template.setMessageConverter(messageConverter);
        template.setConnectionFactory(connectionFactory);
        return template;
    }

   /* @Bean
    public JmsTemplate jmsTopicTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        JmsTemplate template = new JmsTemplate();
        template.setMessageConverter(messageConverter);
        template.setConnectionFactory(connectionFactory);
        template.setPubSubDomain(true);
        return template;
    }*/

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        Map<String, Class<?>> idMappings = new HashMap<>();
        idMappings.put(SendMessageDataDTO.class.getSimpleName(), SendMessageDataDTO.class);
        converter.setTypeIdMappings(idMappings);
        return converter;
    }
//    @Bean
//    public MessageConverter jacksonJmsMessageConverter() {
//        SimpleMessageConverter converter = new SimpleMessageConverter();
////        converter.setTargetType(MessageType.TEXT);
////        converter.setTypeIdPropertyName("_type");
//        return converter;
//    }

    @Bean
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }
}
