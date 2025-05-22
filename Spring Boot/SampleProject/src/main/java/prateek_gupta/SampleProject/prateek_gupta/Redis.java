package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONObject;

public interface Redis {
    Object get(String key,boolean useMap) throws ServiceException;
    void upsert(String key,Object value,boolean useMap) throws ServiceException;
    JSONObject searchKeys(String pattern) throws ServiceException;
    void delete(String key,boolean useMap) throws ServiceException;
}
