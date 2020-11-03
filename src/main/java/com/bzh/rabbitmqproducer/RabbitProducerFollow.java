package com.bzh.rabbitmqproducer;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitProducerFollow {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitProducerFollow.class);

//    private static final String DATA_QUEUE_NAME = "topic.data.4413.191.ttl";
//    private static final String ALARM_QUEUE_NAME = "topic.alarm.4413.191.ttl";
    private static final String FOLLOW_QUEUE_NAME = "topic.follow.4413.191.ttl";

    private static final String IP_ADDRESS = "192.168.1.221";
    private static final String VIRTUAL_HOST = "gjrw191";
    private static final int PORT = 5672; // Rabbitmq服务端默认端口为5672
    private static final String USERNAME = "rabbitmq";
    private static final String PASSWORD = "123456";

    private static final String EXCHANGE_NAME = "exchange_221";
    private static final String ROUTING_KEY_FOLLOW_4413 = "topic.follow.4413.191.ttl";

    private static String decorateFields(String str) {
        DateTimeFormatter fmTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String[] split = str.split(",");
        List<String> list = Arrays.asList(split);
        list.set(4,localDateTime.format(fmTime));
        String join = String.join(",", list);
        return join;
    }



    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        String[] dataMsgs = {
                "\"粤L53687,2,114748885,22970015,2020-10-28 13:45:59,11,1,0,253,440000,441323,2,null,null,null\"",
                "\"粤D21260,2,114362528,23515545,2020-10-28 13:45:54,11,1,96,101,440000,441322,786435,null,null,null\"",
                "\"粤L45915,2,114748760,22970322,2020-10-28 13:46:01,11,1,0,253,440000,441323,2,null,null,null\"",
                "\"粤BS8186,2,113892872,23156600,2020-10-28 13:46:10,30,1,22,250,440000,441322,195,null,null,null\"",
                "\"粤LL5988,2,114267872,22848332,2020-10-28 13:46:01,11,1,0,123,440000,441303,2,null,null,null\""
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setVirtualHost(VIRTUAL_HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 创建一个type=“direct”、持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        // 创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(FOLLOW_QUEUE_NAME,true,false,false,null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(FOLLOW_QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_FOLLOW_4413);
        int index = 0;
        while (true) {
            String message = decorateFields(dataMsgs[index]);
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_FOLLOW_4413, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            Thread.sleep(1002);
            LOG.info("msgcontent: " + message);
            index++;
            if (index >= dataMsgs.length) {
                index = 0;
            }
        }





    }
}
