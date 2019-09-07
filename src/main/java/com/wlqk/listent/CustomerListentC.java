package com.wlqk.listent;

import com.wlqk.model.Student;
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
public class CustomerListentC {

    @RabbitHandler
    @RabbitListener(queues = "hello")
    public void receive(Student msg){
        System.out.println("C---------------------"+msg);
    }
}
