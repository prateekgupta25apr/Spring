package prateek_gupta.SampleProject.openSearch;

import com.fasterxml.jackson.databind.JsonNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.opensearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.opensearch.action.admin.indices.delete.DeleteIndexRequest;
import org.opensearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.opensearch.action.bulk.BulkItemResponse;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.delete.DeleteRequest;
import org.opensearch.action.delete.DeleteResponse;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.action.support.master.AcknowledgedResponse;
import org.opensearch.action.update.UpdateRequest;
import org.opensearch.action.update.UpdateResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.core.CountRequest;
import org.opensearch.client.core.CountResponse;
import org.opensearch.client.indices.*;
import org.opensearch.common.settings.Settings;
import org.opensearch.common.xcontent.*;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.index.reindex.BulkByScrollResponse;
import org.opensearch.index.reindex.DeleteByQueryRequest;
import org.opensearch.search.SearchModule;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.base.SampleProjectException;
import prateek_gupta.SampleProject.utils.Util;

import java.util.ArrayList;

@Service
public class OpenSearchService {
    @Autowired
    RestHighLevelClient client;

    public boolean indexExists(String indexName) throws Exception {
        boolean result;
        try {
            // Create the request
            GetIndexRequest request = new GetIndexRequest(indexName);

            result = client.indices().exists(request, RequestOptions.DEFAULT);

        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject getIndex(String indexName) throws Exception {
        JSONObject result = new JSONObject();
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);


            if (indexExists(indexName)) {
                GetIndexResponse response = client.indices().get(request,
                        RequestOptions.DEFAULT);
                if (response != null) {
                    result.put("aliases", response.getAliases().get(indexName));
                    result.put("settings", response.getSettings().get(indexName).toString());
                    result.put("mappings", response.getMappings().get(indexName).
                            getSourceAsMap().get("properties"));
                }
            } else
                result.put("message", "Index doesn't exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject createIndex(
            String indexName, String source, String aliases, String settings, String mappings)
            throws Exception {
        JSONObject result = new JSONObject();
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            if (StringUtils.isNotBlank(source))
                request.source(source, XContentType.JSON);
            else {
                request.settings(settings, XContentType.JSON);
                request.aliases(aliases, XContentType.JSON);
                request.mapping(mappings, XContentType.JSON);
            }

            if (!indexExists(indexName)) {
                CreateIndexResponse response = client.indices().create(request,
                        RequestOptions.DEFAULT);
                if (response != null && response.isAcknowledged()) {
                    result.put("message", "Index created successfully");
                }
            } else
                result.put("message", "Index already exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject updateIndex(
            String indexName, String settings, String addAlias, String removeAlias,
            String mappings) throws Exception {
        JSONObject result = new JSONObject();
        try {
            if (indexExists(indexName)) {
                AcknowledgedResponse response = null;

                if (StringUtils.isNotBlank(settings)) {
                    UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);
                    request.settings(settings, XContentType.JSON);
                    response = client.indices().putSettings(request,
                            RequestOptions.DEFAULT);
                }

                if (StringUtils.isNotBlank(addAlias) || StringUtils.isNotBlank(removeAlias)) {
                    IndicesAliasesRequest request = new IndicesAliasesRequest();
                    IndicesAliasesRequest.AliasActions aliasAction;

                    if (StringUtils.isNotBlank(addAlias))
                        aliasAction = IndicesAliasesRequest.AliasActions.add()
                                .index(indexName).alias(addAlias);
                    else
                        aliasAction = IndicesAliasesRequest.AliasActions.remove()
                                .index(indexName).alias(removeAlias);

                    request.addAliasAction(aliasAction);
                    response = client.indices().updateAliases(request, RequestOptions.DEFAULT);
                }

                if (StringUtils.isNotBlank(mappings)) {
                    PutMappingRequest request = new PutMappingRequest(indexName);
                    request.source(mappings, XContentType.JSON);
                    response = client.indices().putMapping(request, RequestOptions.DEFAULT);
                }


                if (response != null && response.isAcknowledged()) {
                    result.put("message", "Index updated successfully");
                } else
                    result.put("message", "Nothing to update");
            } else
                result.put("message", "Index doesn't exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject deleteIndex(String indexName) throws Exception {
        JSONObject result = new JSONObject();
        try {
            if (indexExists(indexName)) {
                DeleteIndexRequest request = new DeleteIndexRequest(indexName);
                AcknowledgedResponse response = client.indices().delete(request,
                        RequestOptions.DEFAULT);

                if (response != null && response.isAcknowledged()) {
                    result.put("message", "Index deleted successfully");
                }
            } else
                result.put("message", "Index doesn't exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject getRecord(String indexName, String recordId)
            throws Exception {
        JSONObject result = new JSONObject();
        try {
            if (indexExists(indexName)) {
                GetRequest getRequest = new GetRequest(indexName, recordId);
                GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
                if (getResponse.isExists())
                    result = JSONObject.fromObject(getResponse.getSourceAsString());

            } else
                result.put("message", "Index doesn't exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject upsertRecord(String indexName, String recordId,
                                   String data, boolean bulk)
            throws Exception {
        JSONObject result = new JSONObject();
        try {
            if (indexExists(indexName)) {
                IndexRequest request = new IndexRequest(indexName)
                        .id(recordId)
                        .source(data, XContentType.JSON);
                if (!bulk) {
                    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
                    result.put("result", response.getResult().name());
                } else {
                    BulkRequest bulkRequest = new BulkRequest();
                    bulkRequest.add(request);

                    BulkResponse bulkResponse = client.bulk(bulkRequest,
                            RequestOptions.DEFAULT);
                    for (BulkItemResponse bulkItemResponse : bulkResponse.getItems())
                        result.put("result",
                                bulkItemResponse.getResponse().getResult().name());
                }

            } else
                result.put("message", "Index doesn't exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject partialUpdateRecord(String indexName,
                                          String recordId, String data, boolean bulk)
            throws Exception {
        JSONObject result = new JSONObject();
        try {
            if (indexExists(indexName)) {
                UpdateRequest request = new UpdateRequest(indexName, recordId)
                        .doc(data, XContentType.JSON);

                if (!bulk) {
                    UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
                    result.put("result", response.getResult().name());
                } else {
                    BulkRequest bulkRequest = new BulkRequest();
                    bulkRequest.add(request);

                    BulkResponse bulkResponse = client.bulk(bulkRequest,
                            RequestOptions.DEFAULT);
                    for (BulkItemResponse bulkItemResponse : bulkResponse.getItems())
                        result.put("result",
                                bulkItemResponse.getResponse().getResult().name());
                }

            } else
                result.put("message", "Index doesn't exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JSONObject deleteRecord(String indexName, String recordId
            , boolean bulk) throws Exception {
        JSONObject result = new JSONObject();
        try {
            if (indexExists(indexName)) {
                DeleteRequest request = new DeleteRequest(indexName, recordId);

                if (!bulk) {
                    DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
                    result.put("result", response.getResult().name());
                } else {
                    BulkRequest bulkRequest = new BulkRequest();
                    bulkRequest.add(request);

                    BulkResponse bulkResponse = client.bulk(bulkRequest,
                            RequestOptions.DEFAULT);
                    for (BulkItemResponse bulkItemResponse : bulkResponse.getItems())
                        result.put("result",
                                bulkItemResponse.getResponse().getResult().name());
                }

            } else
                result.put("message", "Index doesn't exists");
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
        return result;
    }

    public JsonNode searchRecord(String index,
                                 String searchJSON)
            throws Exception {
        try {
            SearchRequest searchRequest = new SearchRequest(index);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.wrapperQuery(searchJSON));

            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            return Util.getObjectMapper().readTree(response.toString());
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
    }

    public JsonNode countRecord(String index, String queryJSON)
            throws Exception {
        try {
            CountRequest countRequest = new CountRequest(index);

            countRequest.query(QueryBuilders.wrapperQuery(queryJSON));

            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
            return Util.getObjectMapper().valueToTree(countResponse);
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
    }

    public JsonNode deleteByQueryRecord(String index,
                                        String queryJSON)
            throws Exception {
        try {
            DeleteByQueryRequest request = new DeleteByQueryRequest(index);

            request.setQuery(QueryBuilders.wrapperQuery(queryJSON));

            BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);
            return Util.getObjectMapper().valueToTree(response);
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
    }

    public JsonNode aggregateRecord(String index,
                                    String searchJSON)
            throws Exception {
        try {
            SearchRequest searchRequest = new SearchRequest(index);

            // Loading Query Builder parsers and Aggregations parsers
            SearchModule searchModule = new SearchModule(
                    Settings.EMPTY,
                    false,
                    new ArrayList<>()
            );

            // Creating an object of NamedXContentRegistry to hold all the Query Builder
            // parsers and Aggregations parsers to be used
            NamedXContentRegistry registry =
                    new NamedXContentRegistry(searchModule.getNamedXContents());

            // Creating an object of XContentParser to parse the JSON into Java objects to
            // be used for querying opensearch

            XContentParser parser = XContentType.JSON.xContent().createParser(registry,
                    DeprecationHandler.IGNORE_DEPRECATIONS, searchJSON);

            searchRequest.source(SearchSourceBuilder.fromXContent(parser));

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            return Util.getObjectMapper().readTree(response.toString());
        } catch (Exception e) {
            SampleProjectException.logException(e);
            throw new Exception();
        }
    }
}
