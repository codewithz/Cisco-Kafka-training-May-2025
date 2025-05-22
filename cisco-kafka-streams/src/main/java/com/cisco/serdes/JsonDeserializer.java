package com.cisco.serdes;


import org.apache.kafka.common.serialization.Deserializer;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonDeserializer implements Deserializer<JSONObject> {

    @Override
    public JSONObject deserialize(String topic, byte[] data) {
        return data == null ? null : new JSONObject(new String(data, StandardCharsets.UTF_8));
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}

