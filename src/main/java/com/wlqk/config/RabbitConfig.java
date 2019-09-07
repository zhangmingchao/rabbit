package com.wlqk.config;

/**
 * @author zc
 * @title: RabbitConfig
 * @projectName rabbit
 * @description:
 * @date 2019/8/27 22:27
 */
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {


    @Value("${spring.rabbitmq.queuenames.directoryConditions}")
    private String directoryConditions;

    @Value("${server.port}")
    private String  port;


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        //数据转换为json存入消息队列
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean("directoryConditions")
    public Queue directoryConditions(RabbitAdmin rabbitAdmin){
        String queueName = "";
        try {
            queueName = directoryConditions + (InetAddress.getLocalHost().getHostAddress()+":"+port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("队列名字--------------------"+queueName);
        Queue queue = new Queue(queueName,true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }
}
