package prateek_gupta.sample_project.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    @KafkaListener(topics = "test", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
    }
}
