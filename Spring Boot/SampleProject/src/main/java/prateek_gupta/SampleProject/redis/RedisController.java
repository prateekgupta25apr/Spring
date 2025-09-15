package prateek_gupta.SampleProject.redis;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.prateek_gupta.Redis;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired(required = false)
    Redis redis;

    @GetMapping("get")
    ResponseEntity<ObjectNode> get(@RequestParam String key,
                                   @RequestParam(required = false) String useMap) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("REDIS_ENABLE",true);

            if (StringUtils.isNotBlank(key)) {
                Object value= redis.get(key,Boolean.parseBoolean(useMap));
                response = Util.getSuccessResponse(
                        "Successfully fetched the value", value);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("upsert")
    ResponseEntity<ObjectNode> upsert(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("REDIS_ENABLE",true);

            String key=request.getParameter("key");
            String value=request.getParameter("value");
            String useMap=request.getParameter("useMap");
            if (key!=null) {
                redis.upsert(key,value,Boolean.parseBoolean(useMap));
                response = Util.getSuccessResponse("Successfully saved the key : "+key);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("search_keys")
    ResponseEntity<ObjectNode> searchKeys(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("REDIS_ENABLE",true);

            String pattern=request.getParameter("pattern");
            JSONObject value = redis.searchKeys(pattern);
            response = Util.getSuccessResponse(
                    "Successfully fetched the value", value);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @DeleteMapping("delete")
    ResponseEntity<ObjectNode> delete(@RequestParam String key,
                                   @RequestParam(required = false) String useMap) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("REDIS_ENABLE",true);

            if (StringUtils.isNotBlank(key)) {
                redis.delete(key,Boolean.parseBoolean(useMap));
                response = Util.getSuccessResponse("Successfully deleted the key");
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }
}
