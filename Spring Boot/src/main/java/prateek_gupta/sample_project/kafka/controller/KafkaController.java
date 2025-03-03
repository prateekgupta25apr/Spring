package prateek_gupta.sample_project.kafka.controller;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.sample_project.base.SampleProjectException;
import prateek_gupta.sample_project.kafka.service.KafkaService;
import prateek_gupta.sample_project.utils.Util;

import java.util.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private static final Logger log =
            LoggerFactory.getLogger(KafkaController.class);
    @Autowired
    KafkaService kafkaService;



    @PostMapping("/send")
    ResponseEntity<JSONObject> sendMessage(@RequestParam String topic,
                                           @RequestParam String message) {
        log.info("Entering sendMessage()");
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(topic) &&StringUtils.isNotBlank(message)) {
                kafkaService.sendMessage(topic, message);
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
        log.info("Exiting sendMessage()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get_all_topics")
    ResponseEntity<JSONObject> getAllTopics() {
        log.info("Entering getAllTopics()");
        JSONObject response;

        try (AdminClient adminClient = kafkaService.getAdminClient()) {
            JSONObject responseData=new JSONObject();
            responseData.put("topics",adminClient.listTopics().names().get());
            response = Util.getResponse(true,
                    "Successfully fetched topics",responseData);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }
        log.info("Exiting getAllTopics()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create_topic")
    public ResponseEntity<JSONObject> createTopic(String topicName,
                                                  int partitions, short replicationFactor) {
        log.info("Entering createTopic()");
        JSONObject response;

        try (AdminClient adminClient = kafkaService.getAdminClient()) {
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            CreateTopicsResult result =
                    adminClient.createTopics(Collections.singletonList(newTopic));
            KafkaFuture<Void> future = result.values().get(topicName);
            future.get();
            response = Util.getResponse(true,
                    "Successfully created the topic : "+topicName,null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting createTopic()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update_topic_increase_partition")
    public ResponseEntity<JSONObject> updateTopicIncreasePartition(String topicName,
                                                                   int partitions) {
        log.info("Entering increasePartition()");
        JSONObject response;

        try (AdminClient adminClient = kafkaService.getAdminClient()) {
            Map<String, NewPartitions> partitionsMap = Collections.singletonMap(topicName,
                    NewPartitions.increaseTo(partitions));
            CreatePartitionsResult result = adminClient.createPartitions(partitionsMap);
            result.all().get();
            response = Util.getResponse(true,
                    "Successfully updated the topic's partition to : "+partitions,
                    null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting increasePartition()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("update_topic")
    public ResponseEntity<JSONObject> updateTopic(String topicName, String configKey,
                                                  String configValue) {
        log.info("Entering updateConfig()");
        JSONObject response;

        try (AdminClient adminClient = kafkaService.getAdminClient()) {
            ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            AlterConfigOp op = new AlterConfigOp(new ConfigEntry(configKey, configValue),
                    AlterConfigOp.OpType.SET);
            Map<ConfigResource, Collection<AlterConfigOp>> updateConfigs =
                    Collections.singletonMap(resource, Collections.singletonList(op));

            AlterConfigsResult result = adminClient.incrementalAlterConfigs(updateConfigs);
            result.all().get();
            response = Util.getResponse(true,
                    "Successfully updated the topic : "+topicName,null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting updateConfig()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("get_topic")
    public ResponseEntity<JSONObject> getTopic(String topicName) {
        log.info("Entering getTopic()");
        JSONObject response;

        try (AdminClient adminClient = kafkaService.getAdminClient()) {
            DescribeTopicsResult describeTopicsResult =
                    adminClient.describeTopics(Collections.singleton(topicName));
            TopicDescription topicDescription =
                    describeTopicsResult.topicNameValues().get(topicName).get();

            JSONObject responseData=new JSONObject();
            responseData.put("name", topicDescription.name());
            JSONArray partitions = new JSONArray();
            for (TopicPartitionInfo partitionInfo : topicDescription.partitions()) {
                JSONObject partition = new JSONObject();
                partition.put("partitionId", partitionInfo.partition());
                partitions.add(partition);
            }
            responseData.put("partitions",partitions);

            ConfigResource topicResource =
                    new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            DescribeConfigsResult describeConfigsResult =
                    adminClient.describeConfigs(Collections.singletonList(topicResource));
            Config configResult = describeConfigsResult.all().get().get(topicResource);

            responseData.put("retention.ms",configResult.get("retention.ms").value());
            response = Util.getResponse(true,
                    "Successfully created the topic : "+topicName,responseData);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting getTopic()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("delete_topic")
    public ResponseEntity<JSONObject> deleteTopic(String topicName) {
        log.info("Entering deleteTopic()");
        JSONObject response;

        try (AdminClient adminClient = kafkaService.getAdminClient()) {
            DeleteTopicsResult result =
                    adminClient.deleteTopics(Collections.singletonList(topicName));
            result.all().get();

            response = Util.getResponse(true,
                    "Successfully deleted the topic : "+topicName,null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting deleteTopic()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

