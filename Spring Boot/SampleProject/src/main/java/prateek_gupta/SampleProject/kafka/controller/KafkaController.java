package prateek_gupta.SampleProject.kafka.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.base.ServiceException;
import prateek_gupta.SampleProject.kafka.service.KafkaService;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    KafkaService kafkaService;

    @PostMapping("/send")
    ResponseEntity<ObjectNode> sendMessage(@RequestParam String topic,
                                           @RequestParam String message) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(topic) && StringUtils.isNotBlank(message)) {
                kafkaService.sendMessage(topic, message);
                response = Util.getSuccessResponse("Message sent to topic: " + topic);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @GetMapping("/get_all_topics")
    ResponseEntity<ObjectNode> getAllTopics() {
        ResponseEntity<ObjectNode> response;
        try {
            JSONObject responseData = new JSONObject();
            responseData.put("topics", kafkaService.getAllTopics());
            response = Util.getSuccessResponse(
                    "Successfully fetched topics", responseData);
        } catch (Exception e) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @GetMapping("get_topic")
    public ResponseEntity<ObjectNode> getTopic(String topicName) {
        ResponseEntity<ObjectNode> response;
        try {
            JSONObject responseData=kafkaService.getTopic(topicName);
            response = Util.getSuccessResponse(
                    "Successfully retrieve details for the topic : " + topicName,
                    responseData);
        } catch (Exception e) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("create_topic")
    public ResponseEntity<ObjectNode> createTopic(String topicName,
                                                  int partitions, short replicationFactor) {
        ResponseEntity<ObjectNode> response;
        try {
            kafkaService.createTopic(topicName, partitions, replicationFactor);
            response = Util.getSuccessResponse(
                    "Successfully created the topic : " + topicName);
        } catch (Exception e) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PutMapping("update_topic_increase_partition")
    public ResponseEntity<ObjectNode> updateTopicIncreasePartition(
            String topicName,int partitions) {
        ResponseEntity<ObjectNode> response;
        try {
            kafkaService.updateTopicIncreasePartition(topicName, partitions);
            response = Util.getSuccessResponse(
                    "Successfully updated the topic's partition to : " + partitions);
        } catch (Exception e) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PutMapping("update_topic")
    public ResponseEntity<ObjectNode> updateTopic(
            String topicName, String configKey,String configValue) {
        ResponseEntity<ObjectNode> response;
        try {
            kafkaService.updateTopic(topicName, configKey, configValue);
            response = Util.getSuccessResponse(
                    "Successfully updated the topic : " + topicName);
        } catch (Exception e) {
            return Util.getErrorResponse(new ServiceException());
        }

        return response;
    }

    @DeleteMapping("delete_topic")
    public ResponseEntity<ObjectNode> deleteTopic(String topicName) {
        ResponseEntity<ObjectNode> response;
        try {
            kafkaService.deleteTopic(topicName);
            response = Util.getSuccessResponse(
                    "Successfully deleted the topic : " + topicName);
        } catch (Exception e) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @GetMapping("get_commited_offset")
    public ResponseEntity<ObjectNode> getCommitedOffset(
            String topicName,int partitionId,String consumerGroupName) {
        ResponseEntity<ObjectNode> response;
        try {
            OffsetAndMetadata offsetAndMetadata=kafkaService.getCommittedOffset(
                    topicName, partitionId, consumerGroupName);
            if (offsetAndMetadata != null) {
                response = Util.getSuccessResponse(
                        "Successfully retrieved the commited offset as : " +
                                offsetAndMetadata.offset());
            } else {
                response = Util.getSuccessResponse(
                        "No committed offset found for the specified partition.");

            }
        } catch (Exception e) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("get_messages")
    public ResponseEntity<ObjectNode> getMessages(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try{
            // Extracting payload
            String dataStr = request.getParameter("data");
            JSONObject data = JSONObject.fromObject(dataStr);
            JSONObject responseData=kafkaService.getMessages(data);
            response = Util.getSuccessResponse(
                    "Successfully fetched the messages" , responseData);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        return response;
    }
}

