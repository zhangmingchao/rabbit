package rabbit;

import com.PersonalDataApplication;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlqk.config.RabbitUtil;
import com.wlqk.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.RabbitUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zc
 * @title: RabbitDemo
 * @projectName pd
 * @description:
 * @date 2019/6/8 0:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PersonalDataApplication.class)
public class RabbitDemo {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    //直接模式 点对点通信
    public void send1(){
        for (int i = 0; i < 300; i++) {
            //rabbitTemplate.convertAndSend("test",i+"要使用--rabbitMQ");
            rabbitTemplate.convertSendAndReceive("test",i+"要使用--rabbitMQ");
        }
    }

    //分裂模式
    @Test
    public void fanoutSend(){
        /**
         * 创建一个分列交换机在rabbitmq中，指定类型fanou
         * 将多个队列与交换机进行绑定
         * 第一个参数：交换机的名字
         * 第二个参数：路由键
         * 第三个参数：发送的消息内容
         */
        rabbitTemplate.convertAndSend("topictest","findAll.user","多个人后天要上班");
    }

//    @Autowired
//    private FanoutExchange fanout;
//
//    AtomicInteger dots = new AtomicInteger(0);
//
//    AtomicInteger count = new AtomicInteger(0);

//    @Scheduled(fixedDelay = 1000, initialDelay = 500)
//    public void send() {
//        StringBuilder builder = new StringBuilder("Hello");
//        if (dots.getAndIncrement() == 3) {
//            dots.set(1);
//        }
//        for (int i = 0; i < dots.get(); i++) {
//            builder.append('.');
//        }
//        builder.append(count.incrementAndGet());
//        String message = builder.toString();
//        rabbitTemplate.convertAndSend(fanout.getName(), "", message);
//        System.out.println(" [x] Sent '" + message + "'");
//    }

    @Test
    public void test1(){
        String msg = "生产出消息";
        System.out.println("生产出消息");
        rabbitTemplate.convertAndSend("test",msg);
    }

    @Test
    public void test2(){
        rabbitTemplate.convertAndSend("topic","","123456");
    }

    @Autowired
    private RabbitUtil rabbitUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test111() throws JsonProcessingException {
        Student student = new Student();
        student.setName("张三");
        student.setSex("男");
        String orderJson = objectMapper.writeValueAsString(student);
        Message message = MessageBuilder
                .withBody(orderJson.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        System.out.println(orderJson);
        rabbitTemplate.convertAndSend("hello",student);
    }
}
