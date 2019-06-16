package com.wlqk.listent;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author zc
 * @title: Receive
 * @projectName rabbit
 * @description:
 * @date 2019/6/16 17:21
 */
@Component
@RabbitListener(queues="test")
public class Receive {


    @RabbitHandler
    public void showMessage(String message){
        if (ObjectUtils.isEmpty(message)){
            System.out.println(message+"是空的");
        }else{
            System.out.println("队列test接收到消息："+message);
        }
    }


}
