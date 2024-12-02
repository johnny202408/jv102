package org.example;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticsearchWriter {
    public static void write() {
        // Connect to Elasticsearch
        RestClientBuilder builder = RestClient.builder(
                new org.apache.http.HttpHost("localhost", 9200, "http")
        );
        try (RestHighLevelClient client = new RestHighLevelClient(builder)) {

            // Document to index
            Map<String, Object> document = new HashMap<>();
            document.put("title", "Introduction to Elasticsearch");
            document.put("author", "John Doe");
            document.put("published_date", "2024-12-01");
            document.put("content", "This is a simple Elasticsearch document example.");

            // Create an IndexRequest
//            IndexRequest request = new IndexRequest("books").id("1").source(document, XContentType.JSON);
//            IndexRequest request = new IndexRequest("books", "_doc", "1").source(document, XContentType.JSON);
            IndexRequest request = new IndexRequest("books",
                                                    "_doc"
                                                    ).source(document, XContentType.JSON);



            // Index the document
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println("Document indexed with ID: " + response.getId());
            System.out.println("Response: " + response.getResult());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
