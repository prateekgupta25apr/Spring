package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.util.*;


public class KafkaImpl implements Kafka {
    private final Logger log = LoggerFactory.getLogger(KafkaImpl.class);

    KafkaTemplate<String, String> kafkaTemplate;

    AdminClient adminClient;

    DefaultKafkaConsumerFactory<String, String> consumerFactory;

    public KafkaImpl(Map<String, Object> kafkaConfig) {
        adminClient=AdminClient.create(kafkaConfig);
        consumerFactory=new DefaultKafkaConsumerFactory<>(kafkaConfig) ;
        kafkaTemplate=new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(kafkaConfig));
    }

    @Override
    public void sendMessage(String topic, String message) throws
            ServiceException {
        log.info("Entering sendMessage()");
        try {
            ListenableFuture<SendResult<String, String>> response =
                    kafkaTemplate.send(topic, message);
            response.addCallback(success -> {
                        if (success != null) {
                            RecordMetadata metadata = success.getRecordMetadata();
                            log.info("Message sent successfully to the topic : {}",
                                    metadata.topic());
                        }
                    },
                    failure -> log.info("Message sending failed: {}", failure.getMessage())
            );
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Entering sendMessage()");
    }

    @Override
    public Set<String> getAllTopics() throws ServiceException {
        log.info("Entering getAllTopics()");
        Set<String> topics;
        try {
            topics = adminClient.listTopics().names().get();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting getAllTopics()");
        return topics;
    }

    @Override
    public JSONObject getTopic(String topicName)
            throws ServiceException {
        log.info("Entering getTopic()");
        JSONObject responseData = new JSONObject();
        try {
            DescribeTopicsResult describeTopicsResult =
                    adminClient.describeTopics(Collections.singleton(topicName));
            TopicDescription topicDescription = describeTopicsResult.topicNameValues().
                    get(topicName).get();


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
            ListOffsetsResult startOffsetsResult =
                    adminClient.listOffsets(startOffsetsSpecs);
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> endOffsets =
                    endOffsetsResult.all().get();
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> startOffsets =
                    startOffsetsResult.all().get();

            List<TopicPartition> entries = new ArrayList<>(startOffsets.keySet());

            Collections.reverse(entries);
            JSONObject partitions = new JSONObject();
            JSONObject partitionInfo = new JSONObject();
            for (TopicPartition topicPartition : entries) {
                partitionInfo.put("messageCount", (endOffsets.get(topicPartition).offset() -
                        startOffsets.get(topicPartition).offset()));
                partitionInfo.put("earliest(start)Offset",
                        startOffsets.get(topicPartition).offset());
                partitionInfo.put("latest(end)Offset",
                        endOffsets.get(topicPartition).offset());
                partitions.put(topicPartition.partition(), partitionInfo);
            }

            responseData.put("partitions", partitions);

            ConfigResource topicResource =
                    new ConfigResource(ConfigResource.Type.TOPIC, topicName);
            DescribeConfigsResult describeConfigsResult =
                    adminClient.describeConfigs(Collections.singletonList(topicResource));
            Config configResult = describeConfigsResult.all().get().get(topicResource);

            responseData.put("retention.ms", configResult.get("retention.ms").value());
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting getTopic()");
        return responseData;
    }

    @Override
    public void createTopic(
            String topicName, int partitions, short replicationFactor)
            throws ServiceException {
        log.info("Entering createTopic()");
        try {
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            CreateTopicsResult result =
                    adminClient.createTopics(Collections.singletonList(newTopic));
            KafkaFuture<Void> future = result.values().get(topicName);
            future.get();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting createTopic()");
    }

    @Override
    public void updateTopicIncreasePartition(
            String topicName, int partitions) throws ServiceException {
        log.info("Entering updateTopicIncreasePartition()");
        try {
            Map<String, NewPartitions> partitionsMap = Collections.singletonMap(topicName,
                    NewPartitions.increaseTo(partitions));
            CreatePartitionsResult result = adminClient.createPartitions(partitionsMap);
            result.all().get();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting updateTopicIncreasePartition()");
    }

    @Override
    public void updateTopic(String topicName, String configKey,
                            String configValue) throws ServiceException {
        log.info("Entering updateTopic()");
        try {
            ConfigResource resource = new ConfigResource(
                    ConfigResource.Type.TOPIC, topicName);
            AlterConfigOp op = new AlterConfigOp(new ConfigEntry(configKey, configValue),
                    AlterConfigOp.OpType.SET);
            Map<ConfigResource, Collection<AlterConfigOp>> updateConfigs =
                    Collections.singletonMap(resource, Collections.singletonList(op));

            AlterConfigsResult result = adminClient.incrementalAlterConfigs(updateConfigs);
            result.all().get();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting updateTopic()");
    }

    @Override
    public void deleteTopic(String topicName)
            throws ServiceException {
        log.info("Entering deleteTopic()");
        try {
            DeleteTopicsResult result =
                    adminClient.deleteTopics(Collections.singletonList(topicName));
            result.all().get();
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Existing deleteTopic()");
    }

    @Override
    public OffsetAndMetadata getCommittedOffset(
            String topicName, int partitionId, String consumerGroupName)
            throws ServiceException {
        log.info("Entering getCommittedOffset()");
        OffsetAndMetadata offsetAndMetadata;
        try {
            TopicPartition topicPartition = new TopicPartition(topicName, partitionId);

            // Fetch the committed offset for the consumer group
            Map<TopicPartition, OffsetAndMetadata> committedOffsets =
                    adminClient.listConsumerGroupOffsets(consumerGroupName)
                            .partitionsToOffsetAndMetadata().get();

            offsetAndMetadata = committedOffsets.get(topicPartition);
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        log.info("Exiting getCommittedOffset()");
        return offsetAndMetadata;
    }

    @Override
    public JSONObject getMessages(JSONObject data)
            throws ServiceException {
        JSONObject responseData = new JSONObject();
        try (KafkaConsumer<String, String> consumer = (KafkaConsumer<String, String>)
                consumerFactory.createConsumer()) {


            Map<TopicPartition, List<Integer>> partitionMapping = new LinkedHashMap<>();
            Map<TopicPartition, JSONArray> partitionMessageMapping = new LinkedHashMap<>();

            JSONObject partitionDetails;
            TopicPartition topicPartition;

            // Preparing Partitions
            for (Object topicName : data.keySet()) {
                for (Object partitionDetailsObject :
                        data.getJSONArray(String.valueOf(topicName))) {
                    partitionDetails = JSONObject.fromObject(partitionDetailsObject);

                    topicPartition = new TopicPartition(String.valueOf(topicName),
                            partitionDetails.getInt("partitionId"));
                    partitionMapping.put(topicPartition,
                            Arrays.asList(
                                    partitionDetails.has("offset") ?
                                            partitionDetails.getInt("offset") : 0,
                                    partitionDetails.has("limit") ?
                                            partitionDetails.getInt("limit") : -1
                            )
                    );

                    partitionMessageMapping.put(topicPartition, new JSONArray());
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
            for (Map.Entry<TopicPartition, List<Integer>> entry :
                    partitionMapping.entrySet())
                consumer.seek(entry.getKey(), entry.getValue().get(0));

            // Polling messages
            JSONArray messages;
            JSONObject recordJSON;

            // Polling messages till partitions are there to be checked
            while (!partitionMapping.isEmpty()) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                // Exiting loop if no more messages left
                if (records.isEmpty())
                    break;

                for (ConsumerRecord<String, String> record : records) {
                    // Exiting loop if no more messages left
                    if (partitionMapping.isEmpty())
                        break;

                    topicPartition = new TopicPartition(record.topic(), record.partition());
                    messages = partitionMessageMapping.getOrDefault(
                            topicPartition, new JSONArray());

                    // Processing the message only if the partition still needs to
                    // be processed and either there is no limit or the limit is
                    // yet to be achieved
                    if (partitionMapping.containsKey(topicPartition) &&
                            (partitionMapping.get(topicPartition).get(1) == -1 ||
                                    (messages.size() < partitionMapping.get(
                                            topicPartition).get(1)))) {
                        recordJSON = new JSONObject();
                        recordJSON.put("offset", record.offset());
                        recordJSON.put("key", record.key());
                        recordJSON.put("value", record.value());

                        messages.add(recordJSON);
                        partitionMessageMapping.put(topicPartition, messages);

                        // Removing partition if limit is achieved
                        if (messages.size() == partitionMapping.get(topicPartition).get(1))
                            partitionMapping.remove(topicPartition);
                    }
                }
            }

            // Preparing Response
            JSONObject topic;
            for (Map.Entry<TopicPartition, JSONArray> entry :
                    partitionMessageMapping.entrySet()) {
                topic = responseData.has(entry.getKey().topic()) ?
                        responseData.getJSONObject(entry.getKey().topic()) : new JSONObject();
                topic.put(entry.getKey().partition(), entry.getValue());
                responseData.put(entry.getKey().topic(), topic);
            }
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException();
        }
        return responseData;
    }
}
