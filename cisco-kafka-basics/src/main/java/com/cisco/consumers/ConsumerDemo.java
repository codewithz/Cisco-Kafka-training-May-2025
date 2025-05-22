package com.cisco.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.DoubleDeserializer;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {

    public static void main(String[] args) {
        String BOOTSTRAP_SERVERS = "localhost:9092";
        // String BOOTSTRAP_SERVERS = "broker-1:29092";
        String TOPIC_NAME = "cisco-tx-topic";
        String GROUP_ID = "cisco-analytics-consumer-group";

        Logger logger= LoggerFactory.getLogger(ConsumerDemo.class);


        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DoubleDeserializer.class.getName());

        // Configures the consumer to automatically commit offsets of messages that have been processed.
        // Setting this to "true" enables the consumer to periodically commit the offsets of the messages it has consumed.
        // This is useful for ensuring that the consumer does not reprocess messages after a restart.
        // However, it may lead to data loss if the consumer crashes before committing the offsets.
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

     // Configures the consumer to reset the offset to the earliest available message
        // if no offset is currently stored for the consumer group.
        // This is useful for ensuring that the consumer starts processing messages
        // from the beginning of the topic when no previous offset exists.


        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //earliest: start from the beginning of the topic
        //latest: start from the end of the topic
        //none: do not start from any offset if no previous offset exists

        // Create Kafka Consumer Object
        KafkaConsumer<String, Double> consumer = new KafkaConsumer<>(properties);

        //Subscribe to the topic
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        // Poll for new messages
        while(true){
            // Polls the Kafka broker for new messages from the subscribed topic(s).
            // The poll operation retrieves records from the topic(s) within the specified timeout duration.
            // In this case, the consumer waits for up to 1 second to fetch records.
            // If no records are available within the timeout, an empty ConsumerRecords object is returned.
            ConsumerRecords<String, Double> records = consumer.poll(Duration.ofSeconds(1));

            // Process the records
            for(ConsumerRecord<String, Double> record: records){
                logger.info("---------------------------------------");
                logger.info("Key: " + record.key());
                logger.info("Value: " + record.value());
                logger.info("Partition: " + record.partition());
                logger.info("Offset: " + record.offset());
            }
        }




    }
}
