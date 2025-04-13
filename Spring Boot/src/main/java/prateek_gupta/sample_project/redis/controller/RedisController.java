package prateek_gupta.sample_project.redis.controller;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.sample_project.base.SampleProjectException;
import prateek_gupta.sample_project.redis.service.RedisService;
import prateek_gupta.sample_project.utils.Util;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    RedisService redisService;

    @GetMapping("get")
    ResponseEntity<JSONObject> get(@RequestParam String key,
                                   @RequestParam(required = false) String useMap) {
        JSONObject response;
        try {

            if (StringUtils.isNotBlank(key)) {
                Object value= redisService.get(key,Boolean.parseBoolean(useMap));
                response = Util.getResponse(true,
                        "Successfully fetched the value", value);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (SampleProjectException exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("upsert")
    ResponseEntity<JSONObject> upsert(HttpServletRequest request) {
        JSONObject response;
        try {
            String key=request.getParameter("key");
            String value=request.getParameter("value");
            String useMap=request.getParameter("useMap");
            if (key!=null) {
                redisService.upsert(key,value,Boolean.parseBoolean(useMap));
                response = Util.getResponse(true,
                        "Successfully saved the key : "+key, null);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (SampleProjectException exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("search_keys")
    ResponseEntity<JSONObject> searchKeys(HttpServletRequest request) {
        JSONObject response;
        try {
            String pattern=request.getParameter("pattern");
            JSONObject value = redisService.searchKeys(pattern);
            response = Util.getResponse(true,
                    "Successfully fetched the value", value);
        } catch (SampleProjectException exception) {
            response = Util.getResponse(false, exception.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
