package org.example;

//public class AliasManager {
//}

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetAliasesRequest;
import org.elasticsearch.client.IndicesClient.
import org.elasticsearch.client.indices.GetAliasesResponse;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AliasManager {
    private final RestHighLevelClient client;
    private final ConcurrentMap<String, Boolean> aliasCache = new ConcurrentHashMap<>();

    public AliasManager(RestHighLevelClient client) {
        this.client = client;
    }

    // Check if alias exists and create if necessary
    public synchronized void ensureAliasExists(String aliasName, String indexName) throws IOException {
        if (aliasCache.containsKey(aliasName)) {
            return; // Alias is already checked and cached
        }

        GetAliasesRequest request = new GetAliasesRequest(aliasName);
        GetAliasesResponse response = client.indices().getAlias(request, RequestOptions.DEFAULT);

        if (!response.getAliases().isEmpty()) {
            aliasCache.put(aliasName, true); // Cache the alias
        } else {
            // Create alias if not exists
            IndicesAliasesRequest aliasRequest = new IndicesAliasesRequest();
            aliasRequest.addAliasAction(
                    IndicesAliasesRequest.AliasActions.add()
                            .index(indexName)
                            .alias(aliasName)
                            .isWriteIndex(true) // Set as write index
            );

            IndicesAliasesResponse aliasResponse = client.indices().updateAliases(aliasRequest, RequestOptions.DEFAULT);
            if (aliasResponse.isAcknowledged()) {
                aliasCache.put(aliasName, true); // Cache the alias
                System.out.println("Alias created: " + aliasName);
            } else {
                System.err.println("Failed to create alias: " + aliasName);
            }
        }
    }
}

