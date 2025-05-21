# Consuming Data from a Kafka Topic

Kafka provides a command-line tool called `kafka-console-consumer` to read data from a topic. Below are the basic commands and explanations.

## Consume Data from a Topic (Latest Messages)

Use the following command to consume messages from the latest offset (i.e., only new messages published after the consumer starts):

```sh
kafka-console-consumer --bootstrap-server <host>:<port> --topic <name_of_topic>
```

**Example:**

```sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic bot_first_topic
```

- `--bootstrap-server`: The Kafka broker address (e.g., `localhost:9092`).
- `--topic`: The name of the topic you want to consume from.

## Consume Data from the Beginning of a Topic

To read all messages from the beginning of the topic (including old messages), add the `--from-beginning` flag:

```sh
kafka-console-consumer --bootstrap-server <host>:<port> --topic <name_of_topic> --from-beginning
```

**Example:**

```sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic bot_first_topic --from-beginning
```

- `--from-beginning`: Reads all available messages in the topic, not just new ones.

---

**Tip:**  
You can stop the consumer at any time by pressing `Ctrl+C`.

**Note:**  
Make sure the topic exists and the Kafka broker is running before executing these commands.