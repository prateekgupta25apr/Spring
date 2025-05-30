package prateek_gupta.SampleProject.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public class Util {
    public static ResponseEntity<ObjectNode> getSuccessResponse(
            String message, Object data){
        return getNewResponse(true,message,data,HttpStatus.OK);
    }

    public static ResponseEntity<ObjectNode> getSuccessResponse(
            String message){
        return getNewResponse(true,message,null,HttpStatus.OK);
    }

    public static ResponseEntity<ObjectNode> getErrorResponse(
            ServiceException serviceException){
        return Util.getNewResponse(
                false,serviceException.exceptionMessage,null,
                serviceException.status);
    }

    public static ResponseEntity<ObjectNode> getNewResponse(
            boolean isSuccess,String message, Object data,HttpStatus status){
        ObjectMapper objectMapper=getObjectMapper();
        ObjectNode responseJSON = objectMapper.createObjectNode();
        responseJSON.put("status", isSuccess?"Success":"Failure");
        responseJSON.put("message", message);
        if (data!=null)
            if (data instanceof String)
                try{
                    responseJSON.set("data", objectMapper.readTree(String.valueOf(data)));
                }catch (Exception e){
                    responseJSON.set("data", objectMapper.valueToTree(data));
                }
            else
                responseJSON.set("data", objectMapper.valueToTree(data));
        return new ResponseEntity<>(responseJSON, status);
    }

    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.addMixIn(ParsedStringTerms.ParsedBucket.class,
                IgnoreParsedBucketMixin.class);
        return objectMapper;
    }

    abstract static class IgnoreParsedBucketMixin {
        @JsonIgnore
        public abstract Number getKeyAsNumber();
    }

}
