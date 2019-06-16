package com.wlqk.listent;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zc
 * @title: Customer1
 * @projectName pd
 * @description:
 * @date 2019/6/8 0:12
 */
@Component
@RabbitListener(queues="test",containerFactory = "containerFactory")
public class Customer1 {
    @RabbitHandler
    public void showMessage(String message){
        System.out.println("队列test_1接收到消息："+message);
    }
}
