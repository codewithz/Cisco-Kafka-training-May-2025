package com.cisco.producer.wikimedia;

import com.cisco.producer.ProducerDemoWithKeys;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {

    public static void main(String[] args) throws InterruptedException {
        final String BOOTSTRAP_SERVERS = "localhost:9092";
        // final String BOOTSTRAP_SERVERS = "broker-1:29092";
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class);
        final String TOPIC_NAME = "cisco.wikimedia.message";

        // Setting the properties for the producer
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        String url="https://stream.wikimedia.org/v2/stream/recentchange";

        EventHandler eventHandler=new WikimediaChangeHandler(producer, TOPIC_NAME);

        EventSource.Builder eventSourceBuilder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = eventSourceBuilder.build();

        // Start the event source
        eventSource.start();


        //We will produce the messages for 10 mins and stop
        TimeUnit.MINUTES.sleep(10);



    }
}
