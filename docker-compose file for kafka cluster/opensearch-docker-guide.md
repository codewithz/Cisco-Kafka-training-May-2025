
# 🚀 OpenSearch with Docker Compose - Quick Start Guide

This guide helps you run OpenSearch and OpenSearch Dashboards using Docker Compose, access the UI, and use Dev Tools to explore indices and data.

---

## 📦 1. Setup Docker Compose

### Step 1: Create `docker-compose.yml`

Paste the following into a file named `docker-compose.yml`:

```yaml
version: '3.7'
services:
  opensearch:
    image: opensearchproject/opensearch:1.2.4
    environment:
      discovery.type: single-node
      plugins.security.disabled: "true" # disable https and logins
      compatibility.override_main_response_version: "true"
    ports:
      - 9200:9200
      - 9600:9600 # required for Performance Analyzer

  opensearch-dashboards:
    image: opensearchproject/opensearch-dashboards:1.2.0
    ports:
      - 5601:5601
    environment:
      OPENSEARCH_HOSTS: '["http://opensearch:9200"]'
      DISABLE_SECURITY_DASHBOARDS_PLUGIN: "true"
```

### Step 2: Start the services

```bash
docker-compose -f docker-compose-opensearch.yml up -d
```

Wait for both containers to be up and healthy.

---

## 🌐 2. Access OpenSearch Dashboards

Open your browser and go to:

```
http://localhost:5601
```

You should see OpenSearch Dashboards running.

---

## 🛠️ 3. Use Dev Tools in Dashboards

Once the dashboard is loaded:

1. Click on **"Dev Tools"** in the left sidebar.
2. Use the console to run queries.

### 🔍 Useful Queries

#### List all indices
```json
GET _cat/indices?v
```

#### List only specific indices (e.g. starting with `log`)
```json
GET _cat/indices/log*?v
```

#### Get index mappings
```json
GET my-index/_mapping
```

#### Get all documents from an index
```json
GET my-index/_search
```

#### Create a new index
```json
PUT my-new-index
```

#### Index a document
```json
POST my-new-index/_doc/1
{
  "title": "Hello OpenSearch",
  "content": "This is a test document"
}
```

---

## ✅ 4. Stopping the Environment

To stop the services:
```bash
docker-compose down
```

---

## 📎 Notes

- No security or login is enabled (due to disabled security plugin).
- You can use `curl` or Postman for API testing at `http://localhost:9200`.
