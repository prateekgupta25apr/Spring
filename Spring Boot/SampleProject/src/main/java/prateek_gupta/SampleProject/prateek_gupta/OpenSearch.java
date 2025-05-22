package prateek_gupta.SampleProject.prateek_gupta;

import com.fasterxml.jackson.databind.JsonNode;
import net.sf.json.JSONObject;

public interface OpenSearch {
    boolean indexExists(String indexName)
            throws ServiceException;

    JSONObject getIndex(String indexName)
            throws ServiceException;

    JSONObject createIndex(
            String indexName, String source, String aliases, String settings,
            String mappings) throws ServiceException;

    JSONObject updateIndex(
            String indexName, String settings, String addAlias, String removeAlias,
            String mappings) throws ServiceException;

    JSONObject deleteIndex(String indexName)
            throws ServiceException;

    JSONObject getRecord(
            String indexName, String recordId) throws ServiceException;

    JSONObject upsertRecord(
            String indexName, String recordId, String data, boolean bulk)
            throws ServiceException;

    JSONObject partialUpdateRecord(
            String indexName, String recordId, String data, boolean bulk)
            throws ServiceException;

    JSONObject deleteRecord(
            String indexName, String recordId , boolean bulk) throws ServiceException;

    JsonNode searchRecord(
            String index, String searchJSON)
            throws ServiceException;

    JsonNode countRecord(String index, String queryJSON)
            throws ServiceException;

    JsonNode deleteByQueryRecord(
            String index,String queryJSON)
            throws ServiceException;

    JsonNode aggregateRecord(
            String index,String searchJSON)
            throws ServiceException;
}
