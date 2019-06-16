//package com.wlqk.listent;
//
//import com.rabbitmq.client.ConnectionFactory;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//
//import java.util.UUID;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * @author zc
// * @title: FixedReplyQueueConfig
// * @projectName rabbit
// * @description:
// * @date 2019/6/10 0:57
// */
//@Configuration
//@PropertySource(value = {"classpath:application.properties"})
//public class FixedReplyQueueConfig {
//
//
////    mq.ip=192.168.1.100
////    mq.port=5672
////    mq.userName=rabbitmq_producer
////    mq.password=123456
////    mq.virtualHost=test_vhosts
//    @Bean
//    public ConnectionFactory connectionFactory(){
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("test_vhosts");
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
////        ExecutorService service= Executors.newFixedThreadPool(20);//500个线程的线程池
////        connectionFactory.setSharedExecutor(service);
//        return connectionFactory;
//    }
//
//    @Bean
//    public CachingConnectionFactory rabbitConnectionFactory(){
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory());
//        cachingConnectionFactory.setPublisherConfirms(true);
//        return cachingConnectionFactory;
//    }
//
//    //定义rabbitmqTemplate
//    @Bean
//    public RabbitTemplate fixedReplyQRabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory());
//        template.setExchange(requestExchange().getName());
//        template.setRoutingKey("PRE_RPC");
//        template.setReplyAddress(requestExchange().getName() + "/" + replyQueue().getName());
//        //template.setReceiveTimeout(60000);
//        template.setReplyTimeout(60000);
//        return template;
//    }
//
//
//    //rabbitmqTemplate监听返回队列的消息
//    @Bean
//    public SimpleMessageListenerContainer replyListenerContainer() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(rabbitConnectionFactory());
//        container.setQueues(replyQueue());
//        container.setMessageListener(fixedReplyQRabbitTemplate());
//        ExecutorService executorService= Executors.newFixedThreadPool(300);  //300个线程的线程池
//        container.setTaskExecutor(executorService);
//        container.setConcurrentConsumers(200);
//        container.setPrefetchCount(5);
//        return container;
//    }
//
//    //请求队列和交换器绑定
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(requestQueue()).to(requestExchange()).with("PRE_RPC");
//    }
//
//    //返回队列和交换器绑定
//    @Bean
//    public Binding replyBinding() {
//        return BindingBuilder.bind(replyQueue())
//                .to(requestExchange())
//                .with(replyQueue().getName());
//    }
//
//    //请求队列
//    @Bean
//    public Queue requestQueue() {
//        String queueName = env.getProperty("mq.pre.queue");
//        boolean durable = Boolean.valueOf(env.getProperty("mq.pre.queue.durable"));
//        boolean exclusive = Boolean.valueOf(env.getProperty("mq.pre.queue.exclusive"));
//        boolean autoDelete = Boolean.valueOf(env.getProperty("mq.pre.queue.autoDelete"));
//        return new Queue(queueName,durable,exclusive,autoDelete);
//    }
//
//    //每个应用实例监听的返回队列
//    @Bean
//    public Queue replyQueue() {
//        String queueName = env.getProperty("mq.prereply.queue")+ UUID.randomUUID().toString();
//        boolean durable = Boolean.valueOf(env.getProperty("mq.prereply.queue.durable"));
//        boolean exclusive = Boolean.valueOf(env.getProperty("mq.prereply.queue.exclusive"));
//        boolean autoDelete = Boolean.valueOf(env.getProperty("mq.prereply.queue.autoDelete"));
//        return new Queue(queueName,durable,exclusive,autoDelete);
//    }
//
//    //交换器
//    @Bean
//    public DirectExchange requestExchange() {
//        return new DirectExchange("PRE_DIRECT_EXCHANGE", false, true);
//    }
//
//
//    @Bean
//    public RabbitAdmin admin() {
//        return new RabbitAdmin(rabbitConnectionFactory());
//    }
//}
