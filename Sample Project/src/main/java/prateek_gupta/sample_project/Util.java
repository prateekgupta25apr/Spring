package prateek_gupta.sample_project;

import net.sf.json.JSONObject;

public class Util {

    public  static JSONObject getResponse(boolean isSuccess,String message,Object data){
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
}
