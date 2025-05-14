package prateek_gupta.SampleProject.openSearch;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.base.SampleProjectException;
import prateek_gupta.SampleProject.utils.Util;

@RestController
@RequestMapping("/open_search")
public class OpenSearchController {
    @Autowired
    OpenSearchService service;

    @GetMapping("get_index")
    ResponseEntity<JSONObject> getIndexController(@RequestParam String indexName) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.getIndex(indexName);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully fetched the index details", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create_index")
    ResponseEntity<JSONObject> createIndexController(
            String indexName,String source,String aliases,String settings,String mappings) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.createIndex(
                        indexName, source,aliases,settings,mappings);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully create the index", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("update_index")
    ResponseEntity<JSONObject> updateIndexController(
            String indexName, String settings, String addAlias, String removeAlias,
            String mappings) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.updateIndex(
                        indexName, settings,addAlias,removeAlias,mappings);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully updated the index", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("delete_index")
    ResponseEntity<JSONObject> deleteIndexController(@RequestParam String indexName) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.deleteIndex(indexName);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully deleted the index", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("get_record")
    ResponseEntity<JSONObject> getRecordController(@RequestParam String indexName,
                                                   @RequestParam String docId) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.getRecord(indexName,docId);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully fetched the record", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("upsert_record")
    ResponseEntity<JSONObject> upsertRecordController(String indexName,
                                                      String docId,String data,boolean bulk) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.upsertRecord(indexName,docId,data,bulk);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully inserted the record", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("partial_update_record")
    ResponseEntity<JSONObject> partialUpdateRecordController(String indexName,
                                                      String docId,String data,boolean bulk) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.partialUpdateRecord(indexName,docId,data,bulk);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully updated the record", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("delete_record")
    ResponseEntity<JSONObject> deleteRecordController(String indexName,
                                                      String docId,boolean bulk) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.deleteRecord(indexName,docId,bulk);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully deleted the record", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("search_record")
    ResponseEntity<JSONObject> searchRecordController(String indexName,String searchJSON) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(indexName)) {
                JSONObject indexDetails = service.searchRecord(indexName,searchJSON);
                if (indexDetails != null) {
                    response = Util.getResponse(true,
                            "Successfully deleted the record", indexDetails);
                } else
                    throw new SampleProjectException(
                            SampleProjectException.ExceptionType.DB_ERROR);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (Exception exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
