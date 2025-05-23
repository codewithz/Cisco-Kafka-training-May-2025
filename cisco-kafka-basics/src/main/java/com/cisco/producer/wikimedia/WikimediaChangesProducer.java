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


      // Enables idempotence for the producer to ensure that messages are not duplicated.
        // This setting guarantees exactly-once delivery semantics when combined with acks=all and retries.
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        // Configures the producer to wait for acknowledgment from all in-sync replicas (ISRs).
        // This ensures high durability of the messages sent by the producer.
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");

        // Sets the maximum number of retries for the producer in case of transient failures.
        // This is set to the maximum possible value to ensure retries are not limited.
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));

        // Configures the producer to use Snappy compression for message batches.
        // This reduces the size of the data sent over the network, improving throughput.
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // Sets the linger time for the producer to 20 milliseconds.
        // This allows the producer to batch more records together, improving throughput at the cost of slight latency.
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");

        // Configures the batch size for the producer to 32 KB.
        // Larger batch sizes improve throughput by sending more data in a single request.
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));

        
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
