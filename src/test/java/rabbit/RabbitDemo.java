package rabbit;

import cn.hutool.core.util.ObjectUtil;
import com.PersonalDataApplication;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlqk.listent.HttpKit;
import com.wlqk.listent.RestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.RabbitAccessor;
import org.springframework.amqp.rabbit.connection.RabbitResourceHolder;
import org.springframework.amqp.rabbit.connection.RabbitUtils;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitProperties rabbitProperties;

    @Test
    public void test1(){
        String get = null;
        try {
            get = HttpKit.Get("http://127.0.0.1:15672/api/exchanges/%2F/topic/bindings/source", "guest", "guest");
            List<Map> list = JSON.parseArray(get,Map.class);
            //获取符合路由的map集合
            List<Map> routingKey = list.stream().filter((map -> "a".equals(map.get("routing_key").toString()))).collect(Collectors.toList());
            //获取队列名字
            List<String> stringList = routingKey.stream().map(map -> map.get("destination").toString()).collect(Collectors.toList());
            for (String s : stringList) {
                System.out.println("队列名字是："+s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(get);
    }
}
