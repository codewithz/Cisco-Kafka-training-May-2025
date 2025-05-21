# Kafka Producer CLI Commands Explained

This guide explains how to use the Kafka Console Producer to send data to Kafka topics from the command line.

---

## 1. Produce Data to a Topic (Using Bootstrap Server)

The following commands start a Kafka producer that sends messages to a specified topic. The `--bootstrap-server` option specifies the Kafka broker address.

```sh
kafka-console-producer --bootstrap-server host:port --topic name_of_topic
```

**Example:**

```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic bot_first_topic
```

After running the command, you can type messages in the terminal, and each line will be sent as a message to the topic.

---

## 2. Produce Data with Additional Properties

You can pass extra producer properties using the `--producer-property` flag. This allows you to customize producer behavior, such as setting acknowledgments.

```sh
kafka-console-producer --bootstrap-server host:port --topic name_of_topic --producer-property key=value
```

**Example:**

```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic bot_first_topic --producer-property acks=all
```

In this example, `acks=all` ensures that all replicas acknowledge the message before it is considered sent.

---

## 3. Producing Data to a Non-Existing Topic

If you try to produce data to a topic that does not exist, Kafka may auto-create the topic (depending on broker configuration). However, it is best practice to create topics explicitly before producing data.

```sh
kafka-console-producer --bootstrap-server host:port --topic name_of_topic_which_doesnt_exists
```

**Example:**

```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic bot_fourth_topic
```

---

> **Note:**  
> Always create a topic before producing data to it to avoid unexpected configurations or issues.
