package prateek_gupta.SampleProject.core;

import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import prateek_gupta.SampleProject.prateek_gupta.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class BeanConfiguration {
    @Value("${AWS_ACCESS_KEY:}")
    String AWS_ACCESS_KEY = "";

    @Value("${AWS_SECRET_KEY:}")
    String AWS_SECRET_KEY = "";

    @Value("${AWS_BUCKET_NAME:}")
    String AWS_BUCKET_NAME = "";

    @Value("${AWS_REGION_NAME:}")
    String AWS_REGION_NAME = "";

    @Value("${REDIS_HOST:}")
    String REDIS_HOST="";

    @Value("${REDIS_PORT:}")
    String REDIS_PORT="";

    @Value("${REDIS_SSL:}")
    String REDIS_SSL="";

    @Value("${REDIS_PASSWORD:}")
    String REDIS_PASSWORD="";

    @Value("${KAFKA_BROKER:}")
    String kafkaBrokers;

    @Value("${KAFKA_SECURITY_PROTOCOL:}")
    String securityProtocol;

    @Value("${KAFKA_SASL_MECHANISM:}")
    String saslMechanism;

    @Value("${KAFKA_CONFIG:}")
    String saslConfig;

    @Value("${OPEN_SEARCH_HOST:}")
    String openSearchHost;

    @Value("${OPEN_SEARCH_PORT:}")
    String openSearchPort;

    @Value("${EMAILS_SEND_GRID_ENABLED:false}")
    public String EMAILS_SEND_GRID_ENABLED;

    // SendGrid Config
    @Value("${EMAILS_SEND_GRID_SERVER:}")
    public String EMAILS_SEND_GRID_SERVER;

    @Value("${EMAILS_SEND_GRID_PORT:0}")
    public String EMAILS_SEND_GRID_PORT;

    @Value("${EMAILS_SEND_GRID_USERNAME:}")
    public String EMAILS_SEND_GRID_USERNAME;

    @Value("${EMAILS_SEND_GRID_PASSWORD:}")
    public String EMAILS_SEND_GRID_PASSWORD;

    // SMTP Config
    @Value("${EMAILS_SMTP_SERVER:}")
    public String EMAILS_SMTP_SERVER;

    @Value("${EMAILS_SMTP_PORT:0}")
    public String EMAILS_SMTP_PORT;

    @Value("${EMAILS_SMTP_USERNAME:}")
    public String EMAILS_SMTP_USERNAME;

    @Value("${EMAILS_SMTP_PASSWORD:}")
    public String EMAILS_SMTP_PASSWORD;

    @Bean
    public AWS aws() throws ServiceException {
        return new AWSImpl(AWS_ACCESS_KEY,AWS_SECRET_KEY,AWS_BUCKET_NAME,AWS_REGION_NAME);
    }


    @Bean
    @Conditional(RedisCondition.class)
    public Redis redisService() {
        return new RedisImpl(REDIS_HOST,REDIS_PORT,REDIS_PASSWORD,REDIS_SSL);
    }

    @Bean
    @Conditional(KafkaCondition.class)
    public Kafka kafkaService() {
        return new KafkaImpl(kafkaConfig());
    }

    @Bean
    @Conditional(KafkaCondition.class)
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory;
        factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(kafkaConfig()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
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

    @Bean
    @Conditional(EmailCondition.class)
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        boolean sendGridEnabled = Boolean.parseBoolean(EMAILS_SEND_GRID_ENABLED);

        if (sendGridEnabled) {

            mailSender.setHost(EMAILS_SEND_GRID_SERVER);
            mailSender.setPort(Integer.parseInt(EMAILS_SEND_GRID_PORT));
            mailSender.setUsername(EMAILS_SEND_GRID_USERNAME);
            mailSender.setPassword(EMAILS_SEND_GRID_PASSWORD);

        } else {

            mailSender.setHost(EMAILS_SMTP_SERVER);
            mailSender.setPort(Integer.parseInt(EMAILS_SMTP_PORT));
            mailSender.setUsername(EMAILS_SMTP_USERNAME);
            mailSender.setPassword(EMAILS_SMTP_PASSWORD);
        }

        Properties javaMailProps = mailSender.getJavaMailProperties();

        javaMailProps.put("mail.smtp.auth", "true");
        javaMailProps.put("mail.smtp.starttls.enable", "true");
        javaMailProps.put("mail.smtp.connectiontimeout", "5000");
        javaMailProps.put("mail.smtp.timeout", "5000");
        javaMailProps.put("mail.smtp.writetimeout", "5000");

        return mailSender;
    }

    @Bean
    @Conditional(OpenSearchCondition.class)
    public OpenSearch openSearchService(){
        return new OpenSearchImpl(openSearchHost,openSearchPort);
    }


    @Bean
    public Cryptography cryptography() throws ServiceException {
        return new CryptographyImpl();
    }

    static class RedisCondition implements Condition {
        @Override
        public boolean matches(@NonNull ConditionContext context,
                               @NonNull AnnotatedTypeMetadata metadata) {
            return StringUtils.isNotBlank(
                    context.getEnvironment().getProperty("REDIS_ENABLE"));
        }
    }

    static class KafkaCondition implements Condition {
        @Override
        public boolean matches(@NonNull ConditionContext context,
                               @NonNull AnnotatedTypeMetadata metadata) {
            return StringUtils.isNotBlank(
                    context.getEnvironment().getProperty("KAFKA_ENABLE"));
        }
    }

    static class OpenSearchCondition implements Condition {
        @Override
        public boolean matches(@NonNull ConditionContext context,
                               @NonNull AnnotatedTypeMetadata metadata) {
            return StringUtils.isNotBlank(
                    context.getEnvironment().getProperty("OPEN_SEARCH_ENABLE"));
        }
    }

    static class EmailCondition implements Condition {
        @Override
        public boolean matches(@NonNull ConditionContext context,
                               @NonNull AnnotatedTypeMetadata metadata) {
            return StringUtils.isNotBlank(
                    context.getEnvironment().getProperty("EMAILS_ENABLED"));
        }
    }
}
