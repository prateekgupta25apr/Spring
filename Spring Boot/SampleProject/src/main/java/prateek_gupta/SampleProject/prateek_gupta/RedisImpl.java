package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RType;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


public class RedisImpl implements Redis {

    private final Logger log = LoggerFactory.getLogger(RedisImpl.class);

    StringRedisTemplate redisTemplateString;

    RedisTemplate<String, Object> redisTemplateObject;

    RedissonClient redissonClient;

    String mapName = "mapped_key_values";

    public RedisImpl(String host, String port, String password,
                     String ssl) {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(Integer.parseInt(port));
        redisStandaloneConfiguration.setPassword(password);


        JedisConnectionFactory jedisConnectionFactory;
        if (StringUtils.isNotBlank(ssl))
        {
            JedisClientConfiguration clientConfig =
                    JedisClientConfiguration.builder()
                            .useSsl()
                            .build();

            jedisConnectionFactory = new JedisConnectionFactory(
                    redisStandaloneConfiguration, clientConfig);
        }else
            jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);

        jedisConnectionFactory.afterPropertiesSet();

        // Initiating RedisTemplate for Object
        redisTemplateObject=new RedisTemplate<>();
        redisTemplateObject.setConnectionFactory(jedisConnectionFactory);
        redisTemplateObject.setKeySerializer(new StringRedisSerializer());
        redisTemplateObject.setValueSerializer(
                new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplateObject.afterPropertiesSet();


        // Initiating RedisTemplate for String
        redisTemplateString=new StringRedisTemplate();
        redisTemplateString.setConnectionFactory(jedisConnectionFactory);
        redisTemplateString.setKeySerializer(new StringRedisSerializer());
        redisTemplateString.setValueSerializer(new StringRedisSerializer());
        redisTemplateString.afterPropertiesSet();


        // Initiating RedissonClient
        Config config = new Config();
        config.setCodec(new org.redisson.client.codec.StringCodec());
        SingleServerConfig singleServerConfig=config.useSingleServer();
        singleServerConfig.setAddress(StringUtils.isNotBlank(ssl) ?
                "rediss://" + host + ":" + port:
                "redis://" + host + ":" + port );

        if(StringUtils.isNotBlank(password))
            singleServerConfig.setPassword(password);
        redissonClient= Redisson.create(config);
    }

    @Override
    public Object get(String key, boolean useMap) throws ServiceException {
        log.info("Entering get()");
        Object value;
        try {
            if (useMap) {
                RMap<String, Object> rMap = redissonClient.getMap(mapName);
                value = rMap.get(key);
            } else
                value = redisTemplateString.opsForValue().get(key);
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting get()");
        return value;
    }

    @Override
    public void upsert(String key, Object value, boolean useMap) throws ServiceException {
        log.info("Entering upsert()");
        try {
            if (useMap) {
                RMap<String, Object> rMap = redissonClient.getMap(mapName);
                rMap.put(key, value);
            } else {
                try {
                    redisTemplateObject.opsForValue().set(key, JSONObject.fromObject(value));
                } catch (Exception e) {
                    redisTemplateString.opsForValue().set(key, String.valueOf(value));
                }
            }
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting upsert()");
    }

    @Override
    public JSONObject searchKeys(String pattern) throws ServiceException {
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
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting searchKeys()");
        return response;
    }

    @Override
    public void delete(String key, boolean useMap) throws ServiceException {
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
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting delete()");
    }
}
