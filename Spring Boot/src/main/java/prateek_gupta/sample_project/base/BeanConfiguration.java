package prateek_gupta.sample_project.base;

import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.HashMap;
import java.util.Map;

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

    @Value("${KAFKA_BROKER:}")
    String kafkaBrokers;

    @Value("${KAFKA_SECURITY_PROTOCOL:}")
    String securityProtocol;

    @Value("${KAFKA_SASL_MECHANISM:}")
    String saslMechanism;

    @Value("${KAFKA_CONFIG:}")
    String saslConfig;

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

    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        StringRedisTemplate redisTemplate=new StringRedisTemplate();
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
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(new org.redisson.client.codec.StringCodec());
        config.useSingleServer().setAddress("redis://"+REDIS_HOST+":"+REDIS_PORT);
        return Redisson.create(config);
    }

    @Bean
    @Conditional(KafkaCondition.class)
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(kafkaConfig()));
    }

    @Bean
    @Conditional(KafkaCondition.class)
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory;

        factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }

    @Bean
    @Conditional(KafkaCondition.class)
    public DefaultKafkaConsumerFactory<String,String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(kafkaConfig()) ;
    }

    @Bean
    @Conditional(KafkaCondition.class)
    public AdminClient adminClient(){
        return AdminClient.create(kafkaConfig()) ;
    }

    @Bean("kafkaConfig")
    public Map<String, Object> kafkaConfig() {
        Map<String, Object> kafkaConfig = new HashMap<>();
        kafkaConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);

        if (StringUtils.isNotBlank(securityProtocol))
            kafkaConfig.put("security.protocol", securityProtocol);

        if(StringUtils.isNotBlank(saslMechanism))
            kafkaConfig.put("sasl.mechanism", saslMechanism);

        if(StringUtils.isNotBlank(saslConfig))
            kafkaConfig.put("sasl.jaas.config", saslConfig);

        kafkaConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        kafkaConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");


        kafkaConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConfig.put("enable.auto.commit", "false");
        return kafkaConfig;
    }

    static class KafkaCondition implements Condition {
        @Override
        public boolean matches(@NonNull ConditionContext context,
                               @NonNull AnnotatedTypeMetadata metadata) {
            return StringUtils.isNotBlank(context.getEnvironment().getProperty("KAFKA_ENABLE"));
        }
    }
}
