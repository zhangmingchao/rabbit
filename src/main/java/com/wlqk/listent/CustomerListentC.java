package com.wlqk.listent;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zc
 * @title: CustomerListentA
 * @projectName rabbit
 * @description:
 * @date 2019/8/23 20:56
 */
@Component
@RabbitListener(queues = "hello")
public class CustomerListentC {

    @RabbitHandler
    public void receive(String msg){
        System.out.println("C---------------------"+msg);
    }
}
