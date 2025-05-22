package com.cisco.consumers.opensearch;

import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class OpenSearchConsumer {

   public static RestHighLevelClient createRestHighLevelClient(){
       String openSearchUrl="http://localhost:9200";

       RestHighLevelClient restHighLevelClient;

       URI uri= URI.create(openSearchUrl);

       String userInfo=uri.getUserInfo();

       if(userInfo==null){
           //Rest Client without authentication
           restHighLevelClient=new RestHighLevelClient(
                   RestClient.builder(
                           new HttpHost(uri.getHost(), uri.getPort(), "http")
                   )
           );
       }else{
            restHighLevelClient=null;
       }
         return restHighLevelClient;
   }

   public static KafkaConsumer createkafkaConsumer(){
       final String BOOTSTRAP_SERVERS = "localhost:9093";
         // final String BOOTSTRAP_SERVERS = "broker-1:29092";

         String GROUP_ID="wikimedia-open-search-group";

            // Setting the properties for the consumer
            Properties properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

            // Create Kafka Consumer Object
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
            return consumer;

   }

    public static void main(String[] args) throws IOException {
        Logger logger= LoggerFactory.getLogger(OpenSearchConsumer.class);

        //Create OpenSearch Client
        RestHighLevelClient openSearchClient=createRestHighLevelClient();

        //We need to create an index in opensearch if it does not exist

        boolean doesIndexExist=openSearchClient
                                    .indices()
                                    .exists(new GetIndexRequest(
                                            "cisco_wikimedia_message"),
                                            RequestOptions.DEFAULT);

        if(!doesIndexExist){
            //Create a new index in opensearch
            CreateIndexRequest createIndexRequest=new CreateIndexRequest("cisco_wikimedia_message");
            openSearchClient.indices().create(createIndexRequest,RequestOptions.DEFAULT);
            logger.info("Index created in OpenSearch");
        }else{
            logger.info("Index already exists in OpenSearch");
        }

        // Create Kafka Consumer
        KafkaConsumer<String, String> consumer=createkafkaConsumer();
        // Subscribe to the topic
        consumer.subscribe(Arrays.asList("cisco.wikimedia.message"));
        // Poll for new messages
        while(true){
            ConsumerRecords<String, String> records=consumer.poll(Duration.ofSeconds(5));

            int recordCount=records.count();
            logger.info("Record count: "+recordCount);

            for(ConsumerRecord<String,String> record:records){
                //Send the record to OpenSearch
                //TO make the record unique let assign a key
                String id=record.topic()+"_"+record.partition()+"_"+record.offset();
                try{
                    IndexRequest indexRequest=new IndexRequest("cisco_wikimedia_message")
                                                .source(record.value(), XContentType.JSON)
                                                .id(id);
                    IndexResponse indexResponse=openSearchClient.index(indexRequest, RequestOptions.DEFAULT);

                    logger.info("Record indexed in OpenSearch with ID: "+indexResponse.getId());


                }catch (IOException e) {
                    logger.error("Error indexing record in OpenSearch", e);
                }

                // Commits the offsets of the messages that have been processed by the consumer.
                // This ensures that the consumer's progress is saved, preventing reprocessing of messages
                // in case of a restart or failure. The commit is performed synchronously, meaning the
                // method will block until the commit is acknowledged by the Kafka broker.
                consumer.commitSync();
                logger.info("Offsets have been committed");
            }
        }
    }


}
