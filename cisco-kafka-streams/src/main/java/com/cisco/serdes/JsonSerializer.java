package com.cisco.serdes;



import org.apache.kafka.common.serialization.Serializer;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonSerializer implements Serializer<JSONObject> {

    @Override
    public byte[] serialize(String topic, JSONObject data) {
        return data == null ? null : data.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}

