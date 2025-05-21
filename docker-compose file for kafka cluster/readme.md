# Kafka + ZooKeeper Cluster with Confluent Platform - Docker Compose Explained

## 🧱 Overview
This Docker Compose file sets up a **multi-node Apache Kafka cluster** using **Confluent Platform components**, including ZooKeeper ensemble, Kafka brokers, Schema Registry, Kafka REST Proxy, ksqlDB, and Conduktor Console for monitoring.

---

## 🧩 Services Breakdown

### 1. **ZooKeeper Ensemble (zookeeper-1, zookeeper-2, zookeeper-3)**
- Each ZooKeeper node runs in a separate container.
- They form a 3-node quorum.
- Uses `ZOO_MY_ID` and `ZOO_SERVERS` for ID and cluster configuration.
- Volumes persist data and transaction logs.

### 2. **Kafka Brokers (broker-1, broker-2, broker-3)**
- Three Kafka brokers configured to use the ZooKeeper ensemble.
- Each broker advertises two listener types:
  - `HOST` — for external tools (localhost).
  - `INTERNAL` — for inter-broker communication.
- Ports:
  - `9092`, `9093`, `9094`: External (HOST) access
  - `29092`, `29093`, `29094`: Internal broker-to-broker access

### 3. **Kafka REST Proxy (rest-proxy)**
- Exposes Kafka topics over HTTP (port `8082`).
- Useful for apps not using native Kafka clients.

### 4. **PostgreSQL (postgresql)**
- Backing database for Conduktor Console.
- Uses default credentials with `scram-sha-256` authentication.

### 5. **Conduktor Console (conduktor-console)**
- Web UI to monitor and interact with Kafka.
- Configured via mounted `platform-config.yaml` file.
- Listens on port `8080`.

### 6. **Conduktor Monitoring (conduktor-monitoring)**
- Optional monitoring container (metrics ingestion).
- Connects to the console via environment variable.

### 7. **Schema Registry (schema-registry)**
- Stores Avro schemas for Kafka message validation.
- Connects to the Kafka cluster via internal ports.
- Accessible on port `8081`.

### 8. **ksqlDB Server (ksqldb-server)**
- Enables stream processing with SQL-like language.
- Connects to Kafka brokers and Schema Registry.
- Accessible on port `8088`.

### 9. **ksqlDB CLI (ksqldb-cli)**
- Lightweight CLI client for interacting with ksqlDB Server.
- Runs interactively using `/bin/sh`.

---

## 🗃️ Volumes
- Each ZooKeeper and Kafka broker has dedicated volumes for:
  - **data**: `/var/lib/zookeeper/data` or `/var/lib/kafka/data`
  - **logs**: `/var/lib/zookeeper/log`

---

## ✅ Usage Instructions
1. Place a `platform-config.yml` file in the root directory for Conduktor.
2. Run:
   ```bash
   docker-compose up -d
   ```
3. Access interfaces:
   - Kafka REST Proxy: `http://localhost:8082`
   - Conduktor UI: `http://localhost:8080`
   - Schema Registry: `http://localhost:8081`
   - ksqlDB: `http://localhost:8088`

---

## 📌 Notes
- Ensure Docker volumes are properly writable for persistence.
- You can scale out brokers and ksqlDB clients by copying and updating configs.
- Conduktor Monitoring is optional but useful for deeper insights.
