package prateek_gupta.sample_project.kafka.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.sample_project.kafka.service.KafkaService;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    KafkaService kafkaKafkaService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String topic, @RequestParam String message) {
        kafkaKafkaService.sendMessage(topic, message);
        return "Message sent to topic: " + topic;
    }
}

