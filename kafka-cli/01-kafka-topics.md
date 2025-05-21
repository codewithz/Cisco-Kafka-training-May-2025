# Kafka Topics CLI Commands

This document provides commonly used `kafka-topics` CLI commands with explanations for managing Kafka topics.

---

## Create a Topic (Zookeeper-based)

```sh
kafka-topics --zookeeper host:port --topic name_of_topic --create \
    --partitions noOfPartitions --replication-factor noOfReplicas
```
- **Explanation:**  
    Creates a new topic using the legacy Zookeeper connection.  
    - `--zookeeper`: Address of the Zookeeper server.
    - `--topic`: Name of the topic to create.
    - `--partitions`: Number of partitions for the topic.
    - `--replication-factor`: Number of replicas for each partition.

---

## Create a Topic (Bootstrap Server)

```sh
kafka-topics --bootstrap-server host:port --topic name_of_topic --create 
    --partitions noOfPartitions --replication-factor noOfReplicas
```
- **Explanation:**  
    Creates a new topic using the Kafka broker directly (recommended for newer Kafka versions).
    - `--bootstrap-server`: Address of the Kafka broker.
    - Other options as above.

**Example:**
```sh
kafka-topics --bootstrap-server localhost:9092 --topic bot_first_topic --create --partitions 3 --replication-factor 1
```
- Creates a topic named `bot_first_topic` with 3 partitions and 1 replica.

---

## List Topics

```sh
kafka-topics --bootstrap-server host:port --list
```
- **Explanation:**  
    Lists all topics in the Kafka cluster.

**Example:**
```sh
kafka-topics --bootstrap-server localhost:9092 --list
```

---

## Describe a Topic

```sh
kafka-topics --bootstrap-server host:port --topic name_of_topic --describe
```
- **Explanation:**  
    Shows details about a specific topic, including partition count, replication factor, and partition assignments.

**Example:**
```sh
kafka-topics --bootstrap-server localhost:9092 --topic bot_first_topic --describe
```

**Sample Output:**
```
Topic: bot_first_topic       TopicId: vvukflr0RoudVYxs0kl4LA PartitionCount: 3       ReplicationFactor: 1    Configs: segment.bytes=1073741824
                Topic: bot_first_topic       Partition: 0    Leader: 0       Replicas: 0     Isr: 0
                Topic: bot_first_topic       Partition: 1    Leader: 0       Replicas: 0     Isr: 0
                Topic: bot_first_topic       Partition: 2    Leader: 0       Replicas: 0     Isr: 0
```

---

## Delete a Topic

```sh
kafka-topics --bootstrap-server host:port --topic name_of_topic --delete
```
- **Explanation:**  
    Deletes the specified topic from the Kafka cluster.

**Example:**
```sh
kafka-topics --bootstrap-server localhost:9092 --topic bot_first_topic --delete
```