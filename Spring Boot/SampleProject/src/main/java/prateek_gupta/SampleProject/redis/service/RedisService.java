package prateek_gupta.SampleProject.redis.service;

import net.sf.json.JSONObject;
import prateek_gupta.SampleProject.base.SampleProjectException;

public interface RedisService {
    Object get(String key,boolean useMap) throws SampleProjectException;
    void upsert(String key,Object value,boolean useMap) throws SampleProjectException;
    JSONObject searchKeys(String pattern) throws SampleProjectException;
    void delete(String key,boolean useMap) throws SampleProjectException;
}
