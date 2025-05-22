package prateek_gupta.SampleProject.openSearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.prateek_gupta.OpenSearch;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

@RestController
@RequestMapping("/open_search")
public class OpenSearchController {
    @Autowired
    OpenSearch service;

    @GetMapping("get_index")
    ResponseEntity<ObjectNode> getIndexController(@RequestParam String indexName) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.getIndex(indexName);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully fetched the index details",
                            indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("create_index")
    ResponseEntity<ObjectNode> createIndexController(
            String indexName,String source,String aliases,String settings,
            String mappings) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.createIndex(
                        indexName, source,aliases,settings,mappings);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully create the index", indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }


    @PutMapping("update_index")
    ResponseEntity<ObjectNode> updateIndexController(
            String indexName, String settings, String addAlias, String removeAlias,
            String mappings) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.updateIndex(
                        indexName, settings,addAlias,removeAlias,mappings);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully updated the index", indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @DeleteMapping("delete_index")
    ResponseEntity<ObjectNode> deleteIndexController(@RequestParam String indexName) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.deleteIndex(indexName);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully deleted the index", indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @GetMapping("get_record")
    ResponseEntity<ObjectNode> getRecordController(@RequestParam String indexName,
                                                   @RequestParam String docId) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.getRecord(indexName,docId);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully fetched the record", indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("upsert_record")
    ResponseEntity<ObjectNode> upsertRecordController(
            String indexName, String docId,String data,boolean bulk) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.upsertRecord(indexName,docId,data,bulk);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully inserted the record", indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }


    @PatchMapping("partial_update_record")
    ResponseEntity<ObjectNode> partialUpdateRecordController(
            String indexName, String docId,String data,boolean bulk) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.partialUpdateRecord(indexName,
                        docId,data,bulk);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully updated the record", indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @DeleteMapping("delete_record")
    ResponseEntity<ObjectNode> deleteRecordController(String indexName,
                                                      String docId,boolean bulk) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.deleteRecord(indexName,docId,bulk);
                if (indexDetails != null) {
                    response = Util.getSuccessResponse(
                            "Successfully deleted the record", indexDetails);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("search_record")
    ResponseEntity<ObjectNode> searchRecordController(String indexName, String searchJSON) {
        Object data;
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                 data = service.searchRecord(indexName,searchJSON);
                if (data!=null) {
                    response =  Util.getSuccessResponse(
                            "Successfully searched the record", data);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("count_record")
    ResponseEntity<ObjectNode> countRecordController(String indexName, String searchJSON) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                 Object data = service.countRecord(indexName,searchJSON);
                if (data!=null) {
                    response = Util.getSuccessResponse(
                            "Successfully fetched the record count", data);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }


    @PostMapping("delete_by_query_record")
    ResponseEntity<ObjectNode> deleteByQueryRecordController(String indexName,
                                                             String searchJSON) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                 Object data = service.deleteByQueryRecord(indexName,searchJSON);
                if (data!=null) {
                    response = Util.getSuccessResponse(
                            "Successfully deleted the records", data);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("aggregate_record")
    ResponseEntity<ObjectNode> aggregateRecordController(
            String indexName, String searchJSON) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                Object data = service.aggregateRecord(indexName,searchJSON);
                if (data!=null) {
                    response = Util.getSuccessResponse(
                            "Successfully aggregated the record", data);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }
}
