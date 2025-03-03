package prateek_gupta.sample_project.kafka.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaService {
    private final Logger log = LoggerFactory.getLogger(KafkaService.class);
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public void sendMessage(String topic, String message) {
        ListenableFuture<SendResult<String,String>> response=kafkaTemplate.send(topic, message);
        response.addCallback(
                success -> {
                    if (success!=null) {
                        RecordMetadata metadata = success.getRecordMetadata();
                        log.info("Message sent successfully to the topic : {}",metadata.topic());
                    }
                },
                failure -> log.info("Message sending failed: {}", failure.getMessage())
        );
    }

    @KafkaListener(topics = "test", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Received message: {}", record.value());
    }

    public AdminClient getAdminClient(){
        Map<String,Object> config=new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return AdminClient.create(config);
    }
}
