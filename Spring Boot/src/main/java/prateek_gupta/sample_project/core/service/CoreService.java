package prateek_gupta.sample_project.core.service;

import prateek_gupta.sample_project.core.vo.Table1VO;

public interface CoreService {
    Table1VO getTable1Details(Integer primaryKey);

    void saveTable1Details(String data);
    void redisSave(String key,String value);
    String redisGet(String key);
    void redisSaveObject(String key,String value);

    Object redisGetObject(String key);
}
