package com.wlqk.listent;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



///**
// * @author zc
// * @title: Customer2
// * @projectName rabbit
// * @description:
// * @date 2019/6/10 0:14
// */
//@Component
//public class Customer2 {
//    @RabbitListener(queues="test",containerFactory = "containerFactory")
//    public void showMessage(String message){
//        System.out.println("队列test_2接收到消息："+message);
//    }
//}
