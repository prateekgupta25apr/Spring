package prateek_gupta.sample_project.core.service.impl;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import prateek_gupta.sample_project.core.dao.Table1Repository;
import prateek_gupta.sample_project.core.entities.Table1Entity;
import prateek_gupta.sample_project.core.service.CoreService;
import prateek_gupta.sample_project.core.vo.Table1VO;

@Service
public class CoreServiceImpl implements CoreService {

    private final Logger log = LoggerFactory.getLogger(CoreServiceImpl.class);

    @Autowired
    Table1Repository table1Repository;

    @Autowired
    StringRedisTemplate redisTemplateString;

    @Autowired
    RedisTemplate<String,Object> redisTemplateObject;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public Table1VO getTable1Details(Integer primaryKey) {
        log.info("getTable1Details started");
        try{
            Table1Entity entity= table1Repository.findByPrimaryKey(primaryKey);
            if (entity != null)
                return entity.toVO();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("getTable1Details ended");
        return null;
    }

    @Override
    public void saveTable1Details(String data) {
        log.info("saveTable1Details started");
        try{
                JSONObject jsonObject=JSONObject.fromObject(data);
                Gson gson=new Gson();
                Table1VO table1VO = gson.fromJson(
                        jsonObject.get("data").toString(), Table1VO.class);

            table1Repository.save(table1VO.toEntity());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("saveTable1Details ended");
    }

    @Override
    public void redisSave(String key,String value) {
        log.info("redisSave started");
        try{
            redisTemplateString.opsForValue().set(key,value);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("redisSave ended");
    }

    @Override
    public String redisGet(String key) {
        log.info("redisGet started");
        String value=null;
        try{
            value= redisTemplateString.opsForValue().get(key);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("redisGet ended");
        return value;
    }

    @Override
    public void redisSaveObject(String key, String value) {
        log.info("redisSaveObject started");
        try{
            Table1VO table1VO = new Gson().fromJson(value, Table1VO.class);
            redisTemplateObject.opsForValue().set(key,table1VO);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("redisSaveObject ended");
    }

    @Override
    public Object redisGetObject(String key) {
        log.info("redisGetObject started");
        Object value=null;
        try{
            value= redisTemplateObject.opsForValue().get(key);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("redisGetObject ended");
        return value;
    }

    @Override
    public Object redisGetMap(String key) {
        log.info("redisGetMap started");
        Object value=null;
        try{
            RMap<String,Object> rMap=redissonClient.getMap("mapped_key_values");
            value= rMap.get(key);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("redisGetMap ended");
        return value;
    }

    @Override
    public void redisSaveMap(String key, String value) {
        log.info("redisSaveMap started");
        try{
            RMap<String,Object> rMap=redissonClient.getMap("mapped_key_values");
            rMap.put(key,value);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("redisSaveMap ended");
    }

    @Override
    public void redisAppendMap(String key, String value) {
        log.info("redisAppendMap started");
        try{
            RMap<String,Object> rMap=redissonClient.getMap("mapped_key_values");
            Object response= rMap.get(key);
            JSONArray responseArray=JSONArray.fromObject(response);
            JSONArray valueArray=JSONArray.fromObject(value);
            responseArray.addAll(valueArray);
            rMap.put(key,responseArray);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        log.info("redisAppendMap ended");
    }
}
