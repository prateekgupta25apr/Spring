package prateek_gupta.SampleProject.redis.service.impl;

import net.sf.json.JSONObject;
import org.redisson.api.RMap;
import org.redisson.api.RType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import prateek_gupta.SampleProject.base.SampleProjectException;
import prateek_gupta.SampleProject.redis.service.RedisService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {

    private final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    StringRedisTemplate redisTemplateString;

    @Autowired
    RedisTemplate<String, Object> redisTemplateObject;

    @Autowired
    RedissonClient redissonClient;

    String mapName = "mapped_key_values";

    @Override
    public Object get(String key, boolean useMap) throws SampleProjectException {
        log.info("Entering get()");
        Object value;
        try {
            if (useMap) {
                RMap<String, Object> rMap = redissonClient.getMap(mapName);
                value = rMap.get(key);
            } else
                value = redisTemplateString.opsForValue().get(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SampleProjectException();
        }
        log.info("Exiting get()");
        return value;
    }

    @Override
    public void upsert(String key, Object value, boolean useMap) throws SampleProjectException {
        log.info("Entering upsert()");
        try {
            if (useMap) {
                RMap<String, Object> rMap = redissonClient.getMap(mapName);
                rMap.put(key, value);
            } else {
                try {
                    redisTemplateObject.opsForValue().set(key, JSONObject.fromObject(value));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    redisTemplateString.opsForValue().set(key, String.valueOf(value));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SampleProjectException();
        }
        log.info("Exiting upsert()");
    }

    @Override
    public JSONObject searchKeys(String pattern) throws SampleProjectException {
        log.info("Entering searchKeys()");
        JSONObject response = new JSONObject();
        try {
            for (String key : redissonClient.getKeys().getKeysByPattern(pattern)) {
                if (redissonClient.getKeys().getType(key) == RType.MAP) {
                    RMap<String, Object> map = redissonClient.getMap(mapName);
                    if (map != null && !map.isEmpty())
                        response.put(key, map.readAllMap());
                } else {
                    Object value = redisTemplateString.opsForValue().get(key);
                    if (value != null)
                        response.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SampleProjectException();
        }
        log.info("Exiting searchKeys()");
        return response;
    }

    @Override
    public void delete(String key, boolean useMap) throws SampleProjectException {
        log.info("Entering delete()");
        try {
            if (useMap) {
                RMap<String, Object> rMap = redissonClient.getMap(mapName);
                rMap.remove(key);
            } else {
                try {
                    redisTemplateObject.delete(key);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    redisTemplateString.delete(key);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SampleProjectException();
        }
        log.info("Exiting delete()");
    }
}
