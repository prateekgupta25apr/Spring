package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONObject;

public interface SQS {
    String sendMessage(String queueName, String message) throws ServiceException;
    void receiveMessage(String queueName) throws ServiceException;
}
