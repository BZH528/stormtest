package com.bzh;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestRabbitMQ {

    private static final String ALARM_QUEUE_NAME = "topic.alarm.4413.191.ttl";
    private static final String FOLLOW_QUEUE_NAME = "topic.follow.4413.191.ttl";
    private static final String LOCATE_QUEUE_NAME = "topic.data.4401.191.ttl";
    private static final String BEH_QUEUE_NAME = "topic.highway.beh";

    private static final String IP_ADDRESS = "192.168.10.191";
    private static final String VIRTUAL_HOST = "gjrw191";
    private static final int PORT = 5672; // Rabbitmq服务端默认端口为5672
    private static final String USERNAME = "admin191";
    private static final String PASSWORD = "admin191";


    /**
     * topic.alarm.4401.191.ttl
     * or
     * topic.follow.4413.191.ttl
     * or
     * topic.data.4413.191.ttl
     */
    @Test
    public void testGetSomeRBMQData() throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = new Address[]{new Address(IP_ADDRESS, PORT)};
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);

        Connection connection = factory.newConnection(addresses);
        Channel channel = connection.createChannel();
        channel.basicQos(64);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("msg: " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        channel.basicConsume(LOCATE_QUEUE_NAME, consumer);
        //
        TimeUnit.SECONDS.sleep(1000);
        channel.close();
        connection.close();

    }






}
