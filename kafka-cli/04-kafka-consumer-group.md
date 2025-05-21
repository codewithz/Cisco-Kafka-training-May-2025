# Kafka Consumer Groups: Consuming Data from a Topic

This document explains how to consume data from a Kafka topic using consumer groups, and how to monitor consumer group status.

## Consuming Data from a Topic

To consume data from a Kafka topic as part of a consumer group, use the following command:

```sh
kafka-console-consumer --bootstrap-server <host:port> 
    --topic <name_of_topic> --group <groupName>
```

**Example:**

```sh
kafka-console-consumer --bootstrap-server localhost:9092 
    --topic bot_second_topic --group my-first-app
```

This command starts a consumer that reads messages from the `bot_second_topic` topic as part of the `my-first-app` consumer group.

## Listing Consumer Groups

To list all consumer groups in the Kafka cluster:

```sh
kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

## Describing a Consumer Group

To get detailed information about a specific consumer group, including partition assignments and offsets:

```sh
kafka-consumer-groups --bootstrap-server localhost:9092 
    --describe --group my-first-app
```

**Sample Output:**

| GROUP         | TOPIC           | PARTITION | CURRENT-OFFSET | LOG-END-OFFSET | LAG | CONSUMER-ID | HOST | CLIENT-ID |
|---------------|-----------------|-----------|----------------|----------------|-----|-------------|------|-----------|
| my-first-app  | bot_second_topic| 0         | 8              | 8              | 0   | -           | -    | -         |
| my-first-app  | bot_second_topic| 1         | 8              | 8              | 0   | -           | -    | -         |
| my-first-app  | bot_second_topic| 2         | 8              | 8              | 0   | -           | -    | -         |

- **CURRENT-OFFSET**: The last committed offset for the consumer group.
- **LOG-END-OFFSET**: The latest offset in the partition.
- **LAG**: The difference between the latest offset and the current offset (messages yet to be consumed).

This helps monitor consumer progress and identify if any messages are pending consumption.