# 🧪 Kafka Streams Task: City-wise Order Aggregation

## 🎯 Objective

Create a Kafka Streams application that reads orders from the topic `cisco.orders` and writes to `cisco.city.orders.stats` with:

- Total number of orders per city
- Total amount (sum of price) of orders per city

---

## 📦 Input Topic: `cisco.orders`

Sample input JSON:
```json
{
  "orderId": "fd12-xyz...",
  "customerId": 1005,
  "name": "Sneha",
  "productId": 2450,
  "price": 2200,
  "city": "Pune"
}
```

---

## 📤 Expected Output

Topic: `cisco.city.orders.stats`

Sample output JSON:
```json
{
  "city": "Pune",
  "orderCount": 154,
  "totalAmount": 232000
}
```

---

## 🔍 Hints & API References

| Goal                     | Kafka Streams Function              | Documentation Link                                                                 |
|--------------------------|--------------------------------------|-------------------------------------------------------------------------------------|
| Extract `city`           | `.map()` / `.selectKey()`           | [selectKey()](https://kafka.apache.org/35/javadoc/org/apache/kafka/streams/kstream/KStream.html#selectKey-org.apache.kafka.streams.kstream.KeyValueMapper-) |
| Parse JSON               | `org.json.JSONObject`               | [JSONObject](https://stleary.github.io/JSON-java/org/json/JSONObject.html)         |
| Group by `city`          | `.groupByKey()` / `.groupBy()`      | [groupBy()](https://kafka.apache.org/35/javadoc/org/apache/kafka/streams/kstream/KStream.html#groupBy-org.apache.kafka.streams.kstream.KeyValueMapper-org.apache.kafka.streams.kstream.Grouped-) |
| Count per city           | `.count()`                          | [count()](https://kafka.apache.org/35/javadoc/org/apache/kafka/streams/kstream/KGroupedStream.html#count--)           |
| Sum total amount         | `.aggregate()`                      | [aggregate()](https://kafka.apache.org/35/javadoc/org/apache/kafka/streams/kstream/KGroupedStream.html#aggregate-org.apache.kafka.streams.kstream.Initializer-org.apache.kafka.streams.kstream.Aggregator-org.apache.kafka.streams.kstream.Materialized-) |
| Emit result              | `.toStream().to()`                  | [to()](https://kafka.apache.org/35/javadoc/org/apache/kafka/streams/kstream/KStream.html#to-java.lang.String-)        |

---

## ✅ What to Submit

- Java class file for your Kafka Streams app
- Console output from consuming the `cisco.city.orders.stats` topic

---

## 🧪 Bonus Challenge (Optional)

- Add a timestamp in the output to show last update
- Save the results to a log file or a DB

---

## 📥 Topic Summary

| Topic Name                 | Description                    |
|---------------------------|---------------------------------|
| `cisco.orders`            | Input topic for order events   |
| `cisco.city.orders.stats` | Output topic with city stats   |

Good luck, and happy streaming! 🚀