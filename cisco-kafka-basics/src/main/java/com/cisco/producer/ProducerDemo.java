package com.cisco.producer;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {

    public static void main(String[] args) {

        final String BOOTSTRAP_SERVERS = "localhost:9092";
//        final String BOOTSTRAP_SERVERS = "broker-1:29092";

        final String TOPIC_NAME = "cisco-first-topic";

        // Setting the properties for the producer
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

//        Create Kafka Producer Object
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME,  "Hello From Java Producer");

        // Send the record
        producer.send(record);

        producer.flush();

    }
}
