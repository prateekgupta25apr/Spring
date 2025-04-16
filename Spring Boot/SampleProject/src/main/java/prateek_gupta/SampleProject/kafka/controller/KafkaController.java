package prateek_gupta.SampleProject.kafka.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.base.SampleProjectException;
import prateek_gupta.SampleProject.kafka.service.KafkaService;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private static final Logger log =
            LoggerFactory.getLogger(KafkaController.class);
    @Autowired
    KafkaService kafkaService;

    @Autowired(required = false)
    AdminClient adminClient;

    @Autowired(required = false)
    DefaultKafkaConsumerFactory<String,String> consumerFactory;


    @PostMapping("/send")
    ResponseEntity<JSONObject> sendMessage(@RequestParam String topic,
                                           @RequestParam String message) {
        log.info("Entering sendMessage()");
        JSONObject response;
        try {
            if (StringUtils.isNotBlank(topic) && StringUtils.isNotBlank(message)) {
                kafkaService.sendMessage(topic, message);
                response = Util.getResponse(true,
                        "Message sent to topic: " + topic, null);
            } else
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
        try {
            JSONObject responseData = new JSONObject();
            responseData.put("topics", adminClient.listTopics().names().get());
            response = Util.getResponse(true,
                    "Successfully fetched topics", responseData);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }
        log.info("Exiting getAllTopics()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("get_topic")
    public ResponseEntity<JSONObject> getTopic(String topicName) {
        log.info("Entering getTopic()");
        JSONObject response;

        try {
            DescribeTopicsResult describeTopicsResult =
                    adminClient.describeTopics(Collections.singleton(topicName));
            TopicDescription topicDescription =
                    describeTopicsResult.topicNameValues().get(topicName).get();

            JSONObject responseData = new JSONObject();
            responseData.put("topicName", topicDescription.name());

            Map<TopicPartition, OffsetSpec> endOffsetsSpecs = new HashMap<>();
            Map<TopicPartition, OffsetSpec> startOffsetsSpecs = new HashMap<>();

            for (TopicPartitionInfo partitionInfo : topicDescription.partitions()) {
                TopicPartition topicPartition = new TopicPartition(
                        topicName, partitionInfo.partition());
                endOffsetsSpecs.put(topicPartition, OffsetSpec.latest());
                startOffsetsSpecs.put(topicPartition, OffsetSpec.earliest());
            }
            ListOffsetsResult endOffsetsResult = adminClient.listOffsets(endOffsetsSpecs);
            ListOffsetsResult startOffsetsResult = adminClient.listOffsets(startOffsetsSpecs);
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> endOffsets =
                    endOffsetsResult.all().get();
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> startOffsets =
                    startOffsetsResult.all().get();

            List<TopicPartition> entries=new ArrayList<>(startOffsets.keySet());

            Collections.reverse(entries);
            JSONObject partitions = new JSONObject();
            JSONObject partitionInfo = new JSONObject();
            for (TopicPartition topicPartition :entries) {
                partitionInfo.put("messageCount",(endOffsets.get(topicPartition).offset() -
                        startOffsets.get(topicPartition).offset()));
                partitionInfo.put("earliest(start)Offset",
                        startOffsets.get(topicPartition).offset());
                partitionInfo.put("latest(end)Offset",
                        endOffsets.get(topicPartition).offset());
                partitions.put(topicPartition.partition(),partitionInfo);
            }

            responseData.put("partitions", partitions);

            ConfigResource topicResource =
                    new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            DescribeConfigsResult describeConfigsResult =
                    adminClient.describeConfigs(Collections.singletonList(topicResource));
            Config configResult = describeConfigsResult.all().get().get(topicResource);

            responseData.put("retention.ms", configResult.get("retention.ms").value());
            response = Util.getResponse(true,
                    "Successfully retrieve details for the topic : " + topicName,
                    responseData);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting getTopic()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("create_topic")
    public ResponseEntity<JSONObject> createTopic(String topicName,
                                                  int partitions, short replicationFactor) {
        log.info("Entering createTopic()");
        JSONObject response;

        try {
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            CreateTopicsResult result =
                    adminClient.createTopics(Collections.singletonList(newTopic));
            KafkaFuture<Void> future = result.values().get(topicName);
            future.get();
            response = Util.getResponse(true,
                    "Successfully created the topic : " + topicName, null);
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

        try {
            Map<String, NewPartitions> partitionsMap = Collections.singletonMap(topicName,
                    NewPartitions.increaseTo(partitions));
            CreatePartitionsResult result = adminClient.createPartitions(partitionsMap);
            result.all().get();
            response = Util.getResponse(true,
                    "Successfully updated the topic's partition to : " + partitions,
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

        try {
            ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            AlterConfigOp op = new AlterConfigOp(new ConfigEntry(configKey, configValue),
                    AlterConfigOp.OpType.SET);
            Map<ConfigResource, Collection<AlterConfigOp>> updateConfigs =
                    Collections.singletonMap(resource, Collections.singletonList(op));

            AlterConfigsResult result = adminClient.incrementalAlterConfigs(updateConfigs);
            result.all().get();
            response = Util.getResponse(true,
                    "Successfully updated the topic : " + topicName, null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting updateConfig()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("delete_topic")
    public ResponseEntity<JSONObject> deleteTopic(String topicName) {
        log.info("Entering deleteTopic()");
        JSONObject response;
        try {
            DeleteTopicsResult result =
                    adminClient.deleteTopics(Collections.singletonList(topicName));
            result.all().get();
            response = Util.getResponse(true,
                    "Successfully deleted the topic : " + topicName, null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting deleteTopic()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("get_commited_offset")
    public ResponseEntity<JSONObject> getCommitedOffset(
            String topicName,int partitionId,String consumerGroupName) {
        log.info("Entering getCommitedOffset()");
        JSONObject response = null;
        try {
            TopicPartition topicPartition = new TopicPartition(topicName, partitionId);

            // Fetch the committed offset for the consumer group
            Map<TopicPartition, OffsetAndMetadata> committedOffsets =
                    adminClient.listConsumerGroupOffsets(consumerGroupName)
                            .partitionsToOffsetAndMetadata().get();

            OffsetAndMetadata offsetAndMetadata = committedOffsets.get(topicPartition);
            if (offsetAndMetadata != null) {
                response = Util.getResponse(true,
                        "Successfully retrieved the commited offset as : " +
                                offsetAndMetadata.offset(), null);
            } else {
                response = Util.getResponse(true,
                        "No committed offset found for the specified partition.",
                        null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Exiting getCommitedOffset()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("get_messages")
    public ResponseEntity<JSONObject> getMessages(HttpServletRequest request) {
        log.info("Entering getMessages()");
        JSONObject response;
        try(KafkaConsumer<String,String> consumer= (KafkaConsumer<String, String>)
                consumerFactory.createConsumer()) {
            // Extracting payload
            String dataStr=request.getParameter("data");
            JSONObject data=JSONObject.fromObject(dataStr);

            Map<TopicPartition,List<Integer>> partitionMapping=new LinkedHashMap<>();
            Map<TopicPartition,JSONArray> partitionMessageMapping=new LinkedHashMap<>();

            JSONObject partitionDetails;
            TopicPartition topicPartition;

            // Preparing Partitions
            for (Object topicName : data.keySet()) {
                for (Object partitionDetailsObject :
                        data.getJSONArray(String.valueOf(topicName))) {
                    partitionDetails = JSONObject.fromObject(partitionDetailsObject);

                    topicPartition=new TopicPartition(String.valueOf(topicName),
                            partitionDetails.getInt("partitionId"));
                    partitionMapping.put(topicPartition,
                            Arrays.asList(
                                    partitionDetails.has("offset") ?
                                            partitionDetails.getInt("offset") : 0,
                                    partitionDetails.has("limit") ?
                                            partitionDetails.getInt("limit") : -1
                            )
                    );

                    partitionMessageMapping.put(topicPartition,new JSONArray());
                }
            }


            // Assigning partitions to the consumer and polling to make sure that
            // assignment is done
            consumer.assign(partitionMapping.keySet());
            Set<TopicPartition> assignedPartitions = consumer.assignment();
            while (!assignedPartitions.containsAll(partitionMapping.keySet())) {
                consumer.poll(Duration.ofMillis(100));
                assignedPartitions = consumer.assignment();
            }

            // Seeking partitions to their correct positions
            for (Map.Entry<TopicPartition,List<Integer>> entry: partitionMapping.entrySet())
                consumer.seek(entry.getKey(),entry.getValue().get(0));

            // Polling messages
            JSONArray messages;
            JSONObject recordJSON;

            // Polling messages till partitions are there to be checked
            while (!partitionMapping.isEmpty()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                // Exiting loop if no more messages left
                if (records.isEmpty())
                    break;

                for (ConsumerRecord<String, String> record : records) {
                    // Exiting loop if no more messages left
                    if (partitionMapping.isEmpty())
                        break;

                    topicPartition=new TopicPartition(record.topic(),record.partition());
                    messages=partitionMessageMapping.getOrDefault(topicPartition,new JSONArray());

                    // Processing the message only if the partition still needs to be processed
                    // and either there is no limit or the limit is yet to be achieved
                    if(partitionMapping.containsKey(topicPartition)&&
                            (partitionMapping.get(topicPartition).get(1)==-1||
                            (messages.size()<partitionMapping.get(topicPartition).get(1)))) {
                        recordJSON = new JSONObject();
                        recordJSON.put("offset", record.offset());
                        recordJSON.put("key", record.key());
                        recordJSON.put("value", record.value());

                        messages.add(recordJSON);
                        partitionMessageMapping.put(topicPartition, messages);

                        // Removing partition if limit is achieved
                        if(messages.size()==partitionMapping.get(topicPartition).get(1))
                            partitionMapping.remove(topicPartition);
                    }
                }
            }

            // Preparing Response
            JSONObject responseData = new JSONObject();
            JSONObject topic;
            for(Map.Entry<TopicPartition,JSONArray> entry:partitionMessageMapping.entrySet()){
                topic=responseData.has(entry.getKey().topic())?
                        responseData.getJSONObject(entry.getKey().topic()):new JSONObject();
                topic.put(entry.getKey().partition(),entry.getValue());
                responseData.put(entry.getKey().topic(),topic);
            }

            response = Util.getResponse(true,
                    "Successfully fetched the messages" , responseData);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving topics", e);
        }

        log.info("Exiting getMessages()");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

