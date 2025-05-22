
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
event: message
id: [{"topic":"eqiad.mediawiki.recentchange","partition":0,"timestamp":1747906453001},{"topic":"codfw.mediawiki.recentchange","partition":0,"offset":-1}]
data: {"$schema":"/mediawiki/recentchange/1.0.0","meta":{"uri":"https://www.wikidata.org/wiki/Q63761164","request_id":"aed54472-1f67-41ef-a12a-b3fe8700149f","id":"f9cf0cf6-2bbc-452c-a9a4-5cd3f5c3450a","dt":"2025-05-22T09:34:13Z","domain":"www.wikidata.org","stream":"mediawiki.recentchange","topic":"eqiad.mediawiki.recentchange","partition":0,"offset":5604911050},"id":2422790437,"type":"edit","namespace":0,"title":"Q63761164","title_url":"https://www.wikidata.org/wiki/Q63761164","comment":"/* wbsetclaim-update:2||1|1 */ [[Property:P20]]: [[Q220]]","timestamp":1747906453,"user":"Tiberioclaudio99","bot":false,"notify_url":"https://www.wikidata.org/w/index.php?diff=2351154701&oldid=2351154603&rcid=2422790437","minor":false,"patrolled":true,"length":{"old":43971,"new":44413},"revision":{"old":2351154603,"new":2351154701},"server_url":"https://www.wikidata.org","server_name":"www.wikidata.org","server_script_path":"/w","wiki":"wikidatawiki","parsedcomment":"‎<span dir=\"auto\"><span class=\"autocomment\">Se cambió una afirmación: </span></span> <a href=\"/wiki/Property:P20\" title=\"‎lugar de fallecimiento‎ | ‎sitio donde murió una persona, animal o personaje ficticio (el lugar más específico disponible)‎\"><span class=\"wb-itemlink\"><span class=\"wb-itemlink-label\" lang=\"es\" dir=\"ltr\">lugar de fallecimiento</span> <span class=\"wb-itemlink-id\">(P20)</span></span></a>: <a href=\"/wiki/Q220\" title=\"‎Roma‎ | ‎capital de Italia‎\"><span class=\"wb-itemlink\"><span class=\"wb-itemlink-label\" lang=\"es\" dir=\"ltr\">Roma</span> <span class=\"wb-itemlink-id\">(Q220)</span></span></a>"}

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
