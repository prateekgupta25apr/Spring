package prateek_gupta.SampleProject.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;

public class Util {

    public static JSONObject getResponse(boolean isSuccess,
                                          String message,Object data){
        JSONObject response = new JSONObject();
        if (isSuccess)
            response.put("status", "Success");
        else
            response.put("status", "Failure");
        response.put("message", message);
        if (data!=null)
            response.put("data", data);
        return response;
    }

    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.addMixIn(ParsedStringTerms.ParsedBucket.class, IgnoreParsedBucketMixin.class);
        return objectMapper;
    }

    public static ObjectNode getResponseNew(boolean isSuccess,
                                         String message, Object data){
        ObjectMapper objectMapper=getObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        if (isSuccess)
            response.put("status", "Success");
        else
            response.put("status", "Failure");
        response.put("message", message);
        if (data!=null)
            response.set("data", objectMapper.valueToTree(data));
        return response;
    }

    abstract static class IgnoreParsedBucketMixin {
        @JsonIgnore
        public abstract Number getKeyAsNumber();
    }

}
