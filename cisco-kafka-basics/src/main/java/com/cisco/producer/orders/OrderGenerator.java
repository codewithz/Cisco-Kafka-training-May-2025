package com.cisco.producer.orders;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;


import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class OrderGenerator {

    private static final String[] CITIES = {"Mumbai", "Pune", "Bangalore", "Hyderabad", "Chennai", "Kolkata"};
    private static final String[] NAMES = {"Aman", "Zartab", "Priya", "Sneha", "Ravi", "Neha"};
    private static final String TOPIC = "cisco.orders";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) throws InterruptedException {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        Random random = new Random();

        for (int i = 1; i <= 2000; i++) {
            JSONObject order = new JSONObject();
            order.put("orderId", UUID.randomUUID().toString());
            order.put("customerId", 1000 + random.nextInt(100));
            order.put("name", NAMES[random.nextInt(NAMES.length)]);
            order.put("productId", 2000 + random.nextInt(500));
            order.put("price", 500 + random.nextInt(4500));
            order.put("city", CITIES[random.nextInt(CITIES.length)]);

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, order.getString("orderId"), order.toString());
            producer.send(record);

            System.out.printf("[Order Sent] %s\n", order);
            Thread.sleep(2000); // 2000ms = 2 seconds
        }

        producer.close();
    }
}

