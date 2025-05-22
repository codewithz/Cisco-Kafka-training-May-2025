package com.cisco.streams;


import com.cisco.serdes.JsonDeserializer;
import com.cisco.serdes.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.json.JSONObject;

import java.util.Properties;

public class OrderStream {

    public static void main(String[] args) {

        final String BOOTSTRAP_SERVERS = "localhost:9092";
        final String INPUT_TOPIC = "cisco.orders";
        final String OUTPUT_TOPIC = "cisco.city.orders.stats";

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "city-order-aggregator");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> orderStream = builder.stream(INPUT_TOPIC);

        KTable<String, JSONObject> cityAggregated = orderStream
                .map((key, value) -> {
                    JSONObject json = new JSONObject(value);
                    String city = json.optString("city", "UNKNOWN");
                    int price = json.optInt("price", 0);

                    JSONObject initial = new JSONObject();
                    initial.put("city", city);
                    initial.put("orderCount", 1);
                    initial.put("totalAmount", price);

                    return KeyValue.pair(city, initial.toString());
                })
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .aggregate(
                        () -> new JSONObject().put("orderCount", 0).put("totalAmount", 0).put("city", ""),
                        (key, newValue, aggValue) -> {
                            JSONObject incoming = new JSONObject(newValue);
                            int incomingPrice = incoming.optInt("totalAmount", 0);
                            int incomingCount = incoming.optInt("orderCount", 0);

                            int currentPrice = aggValue.optInt("totalAmount", 0);
                            int currentCount = aggValue.optInt("orderCount", 0);

                            aggValue.put("totalAmount", currentPrice + incomingPrice);
                            aggValue.put("orderCount", currentCount + incomingCount);
                            aggValue.put("city", key);
                            return aggValue;
                        },
                        Materialized.<String, JSONObject, KeyValueStore<Bytes, byte[]>>as("city-aggregation-store")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new Serdes.WrapperSerde<>(new JsonSerializer(), new JsonDeserializer()))
                );

        cityAggregated.toStream()
                .mapValues(value -> value.toString())
                .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

