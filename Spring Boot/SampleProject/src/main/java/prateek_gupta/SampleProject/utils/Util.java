package prateek_gupta.SampleProject.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

public class Util {
    public static ResponseEntity<ObjectNode>
    getSuccessResponse(String message, Object data){
        return getResponse(true,message,data,HttpStatus.OK);
    }

    public static ResponseEntity<ObjectNode>
    getSuccessResponse(String message){
        return getResponse(true,message,null,HttpStatus.OK);
    }

    public static ResponseEntity<ObjectNode> getErrorResponse(
            ServiceException serviceException){
        return Util.getResponse(
                false,serviceException.exceptionMessage,null,
                serviceException.status);
    }

    public static ResponseEntity<ObjectNode> getResponse(
            String message,HttpStatus status){
        return getResponse(null,message,null,status);
    }

    public static ResponseEntity<ObjectNode> getResponse(
            Boolean isSuccess,String message, Object data,HttpStatus status){
        ObjectMapper objectMapper=getObjectMapper();
        ObjectNode responseJSON = objectMapper.createObjectNode();
        if (isSuccess!=null)
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
