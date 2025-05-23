# 🧪 Kafka Streams Case Study: Real-Time Transaction Monitoring

## 🎯 Objective

Design and implement a Kafka Streams pipeline to process financial transactions in real-time. The system should enrich, categorize, aggregate, and monitor transaction activity by city and amount.

---

## 📥 Input Topic: `raw.transactions`

Each message is a JSON object containing:
```json
{
  "transactionId": "abc-123",
  "customerId": "CUST1001",
  "amount": 5400,
  "channel": "ONLINE",
  "city": "Mumbai",
  "timestamp": "2025-05-21T14:42:17.312Z"
}
```

This data is generated using the provided Java class `TransactionDataGenerator.java`, which sends random transactions to the topic `raw.transactions`.

---

## 🛠️ Tasks for Trainees

### ✅ 1. Branch Transaction Stream by Amount
- High-value: `amount >= 5000` → send to `transactions.high`
- Medium-value: `1000 <= amount < 5000` → send to `transactions.medium`
- Low-value: `< 1000` → log to console or ignore

### ✅ 2. Aggregate Per City
- Count total transactions per city
- Calculate total amount per city
- Output results to topic: `city.transaction.stats`

### ✅ 3. Windowed City-Level Analysis
- Count transactions per city in **5-minute tumbling windows**
- Output results to topic: `city.transaction.windowed`

### 🔐 4. Scrutinize High-Risk Transactions
- If `amount >= 8000` and `channel` is `ONLINE` or `MOBILE`, mark the transaction for extra scrutiny
- Add `flag: "HIGH_RISK"` and `reason` field
- Output to `transactions.scrutiny`

### 🎯 5. Enrich Transactions with Risk Level
- Add a `riskLevel` field:
  - `LOW` if amount < 1000
  - `MEDIUM` if 1000 <= amount < 5000
  - `HIGH` if amount >= 5000

### 👤 6. Mask Customer ID in Log Output
- Replace actual digits of `customerId` (e.g., `CUST1234` → `CUST****`) when logging records

### 🔁 7. Re-key by Channel
- Re-key the stream based on the `channel` field for further downstream processing or aggregation

---

## 🧠 Concepts Covered

| Feature                  | Description                          |
|--------------------------|--------------------------------------|
| KStream processing       | Process and filter real-time data    |
| Stream branching         | Categorize by value thresholds       |
| Grouping & aggregation   | Count and sum values by city         |
| Windowed aggregations    | Time-based analytics per key         |
| Conditional enrichment   | Risk detection, field derivation     |
| Key transformation       | Re-keying for channel-based grouping |

---

## 🔧 Generator Code
Use the following Java code to generate random transactions:

```java
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class TransactionDataGenerator {

    private static final String[] CITIES = {"Mumbai", "Pune", "Delhi", "Chennai", "Bangalore", "Hyderabad"};
    private static final String[] CHANNELS = {"ATM", "ONLINE", "BRANCH", "MOBILE"};
    private static final String TOPIC = "raw.transactions";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) throws InterruptedException {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        Random random = new Random();

        for (int i = 1; i <= 1000; i++) {
            String transactionId = UUID.randomUUID().toString();
            String customerId = "CUST" + (1000 + random.nextInt(500));

            JSONObject txn = new JSONObject();
            txn.put("transactionId", transactionId);
            txn.put("customerId", customerId);
            txn.put("amount", 100 + random.nextInt(9900));
            txn.put("channel", CHANNELS[random.nextInt(CHANNELS.length)]);
            txn.put("city", CITIES[random.nextInt(CITIES.length)]);
            txn.put("timestamp", Instant.now().toString());

            producer.send(new ProducerRecord<>(TOPIC, customerId, txn.toString()));
            System.out.println("Sent transaction: " + txn);

            Thread.sleep(1000); // 1 second delay
        }

        producer.close();
    }
}
```

---

## 📦 Suggested Output Topics

| Topic Name                  | Description                                |
|-----------------------------|--------------------------------------------|
| `transactions.high`         | Transactions with amount >= 5000           |
| `transactions.medium`       | Transactions between 1000–4999             |
| `transactions.scrutiny`     | High-risk flagged transactions             |
| `city.transaction.stats`    | Running total + count per city             |
| `city.transaction.windowed` | 5-minute windowed count per city           |

---

## ✅ What to Submit

- Full Java Kafka Streams application
- Console output (or consumer dumps) from all output topics
- (Optional) Screenshots of your topology diagram
- (Optional) Diagram of your Kafka Streams pipeline

---

Let the real-time processing begin! 🚀
