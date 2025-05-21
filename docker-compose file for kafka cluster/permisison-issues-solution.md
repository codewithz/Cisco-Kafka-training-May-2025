```bash
# Fixes permission issues for broker data folders
sudo chown -R 1000:1000 broker-1-data broker-2-data broker-3-data

# Fixes permission issues for Zookeeper data folders
sudo chown -R 1000:1000 zookeeper-1_data zookeeper-2_data zookeeper-3_data

# Fixes permission issues for Zookeeper log folders
sudo chown -R 1000:1000 zookeeper-1_log zookeeper-2_log zookeeper-3_log
```


```bash
# Stop and remove containers defined in the docker-compose file
docker-compose -f docker-compose.yml down
```



```bash
# Start the containers defined in the docker-compose file
docker-compose -f docker-compose.yml up
```

