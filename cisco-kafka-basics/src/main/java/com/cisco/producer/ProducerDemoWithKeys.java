package com.cisco.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class ProducerDemoWithKeys {

    public static void main(String[] args) {
        final String BOOTSTRAP_SERVERS = "localhost:9092";
        // final String BOOTSTRAP_SERVERS = "broker-1:29092";
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class);
        final String TOPIC_NAME = "cisco-tx-topic";

        // Setting the properties for the producer
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class.getName());



        //Array of state names in India
        String[] indianStates = {
                "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
                "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
                "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
                "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttar Pradesh", "Uttarakhand", "West Bengal"
        };

        // Create Kafka Producer Object
            KafkaProducer<String, Double> producer = new KafkaProducer<>(properties);

        for(int i=0;i<25000;i++){
            //Generate random state as a key
            String key=indianStates[(int)(Math.random()*indianStates.length)];

            //Generate random value
            double value=Math.floor(Math.random()*(10000-1000+1)+1000);

            // Create a ProducerRecord with the key and value
            ProducerRecord<String, Double> record = new ProducerRecord<>(TOPIC_NAME, key, value);

            logger.info("Sending message with key " + key + " to Kafka");
            // Send the record

            producer.send(record,((metadata, exception) ->
            {
                if(metadata!=null){
                    logger.info("-----------------------------------");
                    logger.info("Key: " + record.key());
                    logger.info("Value: " + record.value());
                    logger.info("Metadata: " + metadata.toString());
                    logger.info("Topic:"+metadata.topic());
                    logger.info("Partition:"+metadata.partition());
                    logger.info("Offset:"+metadata.offset());
                    logger.info("Timestamp:"+metadata.timestamp());
                } else if (exception != null) {
                    logger.error("Error sending message to Kafka", exception);
                }
            }));

            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }

            producer.flush();


        }



    }
}
