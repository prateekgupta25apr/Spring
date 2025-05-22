package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONObject;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;

import java.util.Set;

public interface Kafka {
    void sendMessage(String topic, String message) throws
            ServiceException;
    Set<String> getAllTopics() throws ServiceException;
    JSONObject getTopic(String topicName)
            throws ServiceException;
    void createTopic(
            String topicName, int partitions, short replicationFactor)
            throws ServiceException;
    void updateTopicIncreasePartition(
            String topicName, int partitions) throws ServiceException;
    void updateTopic(String topicName, String configKey,
                     String configValue) throws ServiceException;
    void deleteTopic(String topicName)
            throws ServiceException;
    OffsetAndMetadata getCommittedOffset(
            String topicName, int partitionId, String consumerGroupName)
            throws ServiceException;
    JSONObject getMessages(JSONObject data)
            throws ServiceException;
}
