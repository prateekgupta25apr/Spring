package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public interface SQS {
    String sendMessage(String queueName, String message) throws ServiceException;
    void pollMessages(String queueName);
    void updateQueueNames(String queueName, boolean isAdded) throws ServiceException;
    List<String> getAllQueues() throws ServiceException;
    JSONObject getQueue(String queueName) throws ServiceException;
    void createQueue(String queueName,String visibilityTimeOut,String retentionPeriod)
            throws ServiceException;
    void updateQueue(String queueName,String attributeName,String attributeValue)
            throws ServiceException;
    void deleteQueue(String queueName) throws ServiceException;
    JSONArray getMessages(String queueName) throws ServiceException;
}
