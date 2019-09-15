package com.wlqk.listent;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitManagementTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 配置rabbitMQ
 * @author
 *
 */
@Configuration
//@Order(-10)
public class RabbitConfig {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }




    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

//    @Override
//    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
//        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
//    }

//    @Bean
//    MessageHandlerMethodFactory messageHandlerMethodFactory() {
//        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
//        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
//        return messageHandlerMethodFactory;
//    }

//    @Bean
//    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
//        return new MappingJackson2MessageConverter();
//    }
//    @Bean
//    public RabbitManagementTemplate rabbitManagementTemplate(){
//        return new RabbitManagementTemplate();
//    }


}

