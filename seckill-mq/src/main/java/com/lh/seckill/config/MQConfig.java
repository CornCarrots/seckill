package com.lh.seckill.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MQConfig {
    private static final Logger log= LoggerFactory.getLogger(MQConfig.class);
    /**
     * 消息队列名
     */
    public static final String SECKILL_QUEUE = "seckill.queue";

    /**
     * 秒杀 routing key, 生产者沿着 routingKey 将消息投递到 exchange 中
     */
    public static final String SK_ROUTING_KEY = "routing.sk";

    @Bean
    public Queue seckillQueue() {
        return new Queue(SECKILL_QUEUE);
    }
    /**
     * 实例化 RabbitTemplate
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }

    /**
     * 此内部类就是把yml的配置数据，进行读取，创建JedisConnectionFactory和JedisPool，以供外部类初始化缓存管理器使用
     * 不了解的同学可以去看@ConfigurationProperties和@Value的作用
     *
     */
    @ConfigurationProperties
    class RabbitMQProperties{
        @Value("${spring.rabbitmq.host}")
        private  String host;
        @Value("${spring.rabbitmq.port}")
        private  int port;
        @Value("${spring.rabbitmq.username}")
        private  String username;
        @Value("${spring.rabbitmq.password}")
        private  String password;

        @Bean
        public ConnectionFactory connectionFactory() {
            CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
            cachingConnectionFactory.setHost(host);
            cachingConnectionFactory.setPort(port);
            cachingConnectionFactory.setUsername(username);
            cachingConnectionFactory.setPassword(password);
            cachingConnectionFactory.setPublisherConfirms(true);
            cachingConnectionFactory.setPublisherReturns(true);
            return cachingConnectionFactory;
        }
    }
}
