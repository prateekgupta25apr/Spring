package prateek_gupta.SampleProject.redis.service;

import net.sf.json.JSONObject;
import prateek_gupta.SampleProject.base.ServiceException;

public interface RedisService {
    Object get(String key,boolean useMap) throws ServiceException;
    void upsert(String key,Object value,boolean useMap) throws ServiceException;
    JSONObject searchKeys(String pattern) throws ServiceException;
    void delete(String key,boolean useMap) throws ServiceException;
}
