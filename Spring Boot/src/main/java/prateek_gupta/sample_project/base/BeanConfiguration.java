package prateek_gupta.sample_project.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BeanConfiguration {
    @Value("${AWS_ACCESS_KEY}")
    String AWS_ACCESS_KEY = "";

    @Value("${AWS_SECRET_KEY}")
    String AWS_SECRET_KEY = "";

    @Value("${REDIS_HOST}")
    String REDIS_HOST="";

    @Value("${REDIS_PORT}")
    String REDIS_PORT="";

    @Bean
    public S3Client s3Client() {
        S3Client s3Client = null;
        DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();
        if (credentialsProvider.resolveCredentials() != null) {
            System.out.println("Access Key: " +
                    credentialsProvider.resolveCredentials().accessKeyId());


            s3Client = S3Client.builder()
                    .credentialsProvider(credentialsProvider)
                    .region(Region.AP_SOUTH_1)
                    .build();
        }
        if (s3Client == null)
            s3Client = S3Client.builder()
                    .region(Region.AP_SOUTH_1)
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY)))
                    .build();

        return s3Client;
    }

    @Bean
    public RedisTemplate<String,String> redisTemplateString(){
        RedisTemplate<String,String>redisTemplate=new RedisTemplate<>();
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(REDIS_HOST);
        config.setPort(Integer.parseInt(REDIS_PORT));

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
        jedisConnectionFactory.afterPropertiesSet();

        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplateObject(){
        RedisTemplate<String,Object>redisTemplate=new RedisTemplate<>();
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(REDIS_HOST);
        config.setPort(Integer.parseInt(REDIS_PORT));

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
        jedisConnectionFactory.afterPropertiesSet();

        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }
}
