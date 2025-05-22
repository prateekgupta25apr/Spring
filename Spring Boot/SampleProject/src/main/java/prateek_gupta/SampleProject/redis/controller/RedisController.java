package prateek_gupta.SampleProject.redis.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.base.ServiceException;
import prateek_gupta.SampleProject.redis.service.RedisService;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    RedisService redisService;

    @GetMapping("get")
    ResponseEntity<ObjectNode> get(@RequestParam String key,
                                   @RequestParam(required = false) String useMap) {
        ResponseEntity<ObjectNode> response;
        try {

            if (StringUtils.isNotBlank(key)) {
                Object value= redisService.get(key,Boolean.parseBoolean(useMap));
                response = Util.getSuccessResponse(
                        "Successfully fetched the value", value);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("upsert")
    ResponseEntity<ObjectNode> upsert(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            String key=request.getParameter("key");
            String value=request.getParameter("value");
            String useMap=request.getParameter("useMap");
            if (key!=null) {
                redisService.upsert(key,value,Boolean.parseBoolean(useMap));
                response = Util.getSuccessResponse("Successfully saved the key : "+key);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("search_keys")
    ResponseEntity<ObjectNode> searchKeys(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            String pattern=request.getParameter("pattern");
            JSONObject value = redisService.searchKeys(pattern);
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
            if (StringUtils.isNotBlank(key)) {
                redisService.delete(key,Boolean.parseBoolean(useMap));
                response = Util.getSuccessResponse("Successfully deleted the key");
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }
}
