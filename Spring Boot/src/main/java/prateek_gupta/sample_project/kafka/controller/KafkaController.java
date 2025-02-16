package prateek_gupta.sample_project.kafka.controller;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.sample_project.base.SampleProjectException;
import prateek_gupta.sample_project.kafka.service.KafkaService;
import prateek_gupta.sample_project.utils.Util;

import java.util.Properties;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private static final Logger log =
            LoggerFactory.getLogger(KafkaController.class);
    @Autowired
    KafkaService kafkaKafkaService;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @PostMapping("/send")
    ResponseEntity<JSONObject> sendMessage(@RequestParam String topic,
                                           @RequestParam String message) {
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(topic) &&StringUtils.isNotBlank(message)) {
                kafkaKafkaService.sendMessage(topic, message);
                response = Util.getResponse(true,
                        "Message sent to topic: " + topic,null);
            }
            else
                throw new SampleProjectException(
                        SampleProjectException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (SampleProjectException exception) {
            response = Util.getResponse(false, exception.getMessage(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get_all_topics")
    ResponseEntity<JSONObject> getAllTopics() {
        JSONObject response;
        // Create Kafka AdminClient
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(config)) {
            JSONObject responseData=new JSONObject();
            responseData.put("topics",adminClient.listTopics().names().get());
            response = Util.getResponse(true,
                    "Successfully fetched topics",responseData);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

