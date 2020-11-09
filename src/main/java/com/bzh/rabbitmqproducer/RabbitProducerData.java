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

public class RabbitProducerData {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitProducerData.class);

    private static final String DATA_QUEUE_NAME = "topic.data.4401.191.ttl";
//    private static final String ALARM_QUEUE_NAME = "topic.alarm.4413.191.ttl";
//    private static final String FOLLOW_QUEUE_NAME = "topic.follow.4413.191.ttl";

    private static final String IP_ADDRESS = "192.168.1.221";
    private static final String VIRTUAL_HOST = "gjrw191";
    private static final int PORT = 5672; // Rabbitmq服务端默认端口为5672
    private static final String USERNAME = "rabbitmq";
    private static final String PASSWORD = "123456";

    private static final String EXCHANGE_NAME = "exchange_221";
    private static final String ROUTING_KEY_DATA_4401 = "topic.data.4401.191.ttl";

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
                "\"35775FAB5CE889240AE0EAFB274E0A53,0,113537600,22838168,2020-11-02 16:59:35,30,0,290,440000,440115,786435,null,null,null\"",
                "\"3DBFE1EE7B819383CE2F536A11205400,0,113301144,23397640,2020-11-02 17:00:28,12,0,100,440000,440114,786627,null,null,null\"",
                "\"DBF13D21A3C1070CF1F631ACB829BA31,0,113511776,22856582,2020-11-02 17:00:22,12,0,0,440000,440115,786627,null,null,null\"",
                "\"37C9EFB66D7B87461CF0CD6CBE66C645,0,113532184,23119984,2020-11-02 17:00:28,12,0,46,440000,440112,819395,null,null,null\"",
                "\"D9677C3999EA889C56BDD044048F64F7,0,113199980,23117700,2020-11-02 17:01:24,11,0,140,450000,440103,2,null,null,null\"",
                "\"DA5BD00878B85D91F434B1B2CE58C855,0,113455513,22864202,2020-11-02 17:00:31,11,92,255,440000,440113,19,null,null,null\"",
                "\"9037F5DBA9226FF48A8C797FF1549C07,0,113479016,23212444,2020-11-02 17:00:25,12,0,263,440000,440111,786435,null,null,null\"",
                "\"828C9E9F4EBAF2A5FD77489E4745CC70,0,113438936,23246268,2020-11-02 17:00:28,11,52,304,440000,440114,786627,null,null,null\"",
                "\"93E041198A7452BF7F5A85D59A3D6696,0,113419032,23257996,2020-11-02 17:00:24,30,0,202,440000,440115,786626,null,null,null\""
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
        channel.queueDeclare(DATA_QUEUE_NAME,true,false,false,null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(DATA_QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_DATA_4401);
        int index = 0;
        while (true) {
            String message = decorateFields(dataMsgs[index]);
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_DATA_4401, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            Thread.sleep(1000);
            LOG.info("msgcontent: " + message);
            index++;
            if (index >= dataMsgs.length) {
                index = 0;
            }
        }





    }
}
