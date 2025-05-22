package com.cisco.producer.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikimediaChangeHandler implements EventHandler {

    Logger logger= LoggerFactory.getLogger(WikimediaChangeHandler.class);
    KafkaProducer<String, String> producer;
    String topic;

    public WikimediaChangeHandler(KafkaProducer<String, String> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    @Override
    public void onOpen() throws Exception {
//        DO NOTHING
    }

    @Override
    public void onClosed() throws Exception {
        producer.flush();
        producer.close();
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        logger.info("New message received");
        logger.info("Event: " + event);
        logger.info("Data: " + messageEvent.getData());
        sendRecord(messageEvent);
    }

    @Override
    public void onComment(String comment) throws Exception {
//        DO NOTHING
    }

    @Override
    public void onError(Throwable t) {
        logger.error("Error in event source", t);
    }

    public void sendRecord(MessageEvent messageEvent) {

        //Send data to producer
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, messageEvent.getData());
        producer.send(record,(metadata, exception) ->{

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
        });
        }
}
