package com.wlqk.listent;

import com.wlqk.model.Student;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author zc
 * @title: CustomerListentA
 * @projectName rabbit
 * @description:
 * @date 2019/8/23 20:56
 */
@Component
public class CustomerListentC {

    @Value("#{directoryConditions.name}")
    private String queueName;

    @RabbitHandler
    @RabbitListener(queues = "#{directoryConditions.name}")
    public void receive(List<Map<String,Object>> msg){
        System.out.println("当前队列："+queueName);
        System.out.println("C---------------------"+msg);
    }
}
