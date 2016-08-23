package com.johnny.nonjunit;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import com.hazelcast.config.*;

import java.util.Map;
import java.util.Queue;

/**
 * Created by johnny on 24/03/2016.
 */
public class GettingStarted {

    public static void main(String[] args) {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("127.0.0.1:5701");
        clientConfig.setGroupConfig(new GroupConfig("hazelcast-group-server", "hazelcast-group-password"));

        HazelcastInstance instance = HazelcastClient.newHazelcastClient(clientConfig);

        Map<Long, String> mapCustomers = instance.getMap("customers");
        mapCustomers.put(1L, "Joe");
        mapCustomers.put(2L, "Ali");
        mapCustomers.put(3L, "Avi");

        mapCustomers.put(1L, "John");

        System.out.println("Customer with key 1: " + mapCustomers.get(1L));
        System.out.println("Map Size:" + mapCustomers.size());

        Queue<String> queue1 = instance.getQueue("queue.1");
        System.out.println("Queue size: " + queue1.size());
        queue1.offer("abc");
        queue1.offer("def");
        queue1.offer("10003");
        System.out.println("First item 1: " + queue1.poll());
        System.out.println("Second item 1: "+ queue1.peek());
        System.out.println("Queue size: " + queue1.size());

        Queue<String> queue2 = instance.getQueue("queue.2");
        System.out.println("Queue size: " + queue2.size());
        queue2.offer("201");
        queue2.offer("202");
        queue2.offer("203");
        System.out.println("First item 2: " + queue2.poll());
        System.out.println("Second item 2: "+ queue2.peek());
        System.out.println("Queue size: " + queue2.size());

        instance.shutdown();
    }

}


