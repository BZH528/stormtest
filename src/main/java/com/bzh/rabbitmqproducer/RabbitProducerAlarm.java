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

public class RabbitProducerAlarm {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitProducerAlarm.class);

//    private static final String DATA_QUEUE_NAME = "topic.data.4413.191.ttl";
    private static final String ALARM_QUEUE_NAME = "topic.alarm.4413.191.ttl";
//    private static final String FOLLOW_QUEUE_NAME = "topic.follow.4413.191.ttl";

    private static final String IP_ADDRESS = "192.168.1.221";
    private static final String VIRTUAL_HOST = "gjrw191";
    private static final int PORT = 5672; // Rabbitmq服务端默认端口为5672
    private static final String USERNAME = "rabbitmq";
    private static final String PASSWORD = "123456";

    private static final String EXCHANGE_NAME = "exchange_221";
    private static final String ROUTING_KEY_ALARM_4413 = "topic.alarm.4413.191.ttl";

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
                "\"粤L57287,2,114894266,22784817,2020-10-27 17:23:46,11,2,1,48,237,440000,441323,3,null,null,null,0A04942E0336A8385B332ABBC2120CF8\"",
                "\"粤L46732,2,114634746,23098612,2020-10-27 17:23:50,30,2,1,23,180,440000,441323,3,null,null,null,20C928CBAEA7701F425465BECDCE796A\"",
                "\"粤L59481,2,114438477,23037645,2020-10-27 17:49:11,11,2,1,47,248,440000,441302,3,null,null,null,EBD2AE48825F114F3CBFCF748E5CA8C8\""
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
        channel.queueDeclare(ALARM_QUEUE_NAME,true,false,false,null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(ALARM_QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_ALARM_4413);
        int index = 0;
        while (true) {
            String message = decorateFields(dataMsgs[index]);
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_ALARM_4413, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            Thread.sleep(1001);
            LOG.info("msgcontent: " + message);
            index++;
            if (index >= dataMsgs.length) {
                index = 0;
            }
        }





    }
}
