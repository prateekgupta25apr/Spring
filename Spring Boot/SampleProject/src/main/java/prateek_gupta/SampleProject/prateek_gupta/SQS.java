package prateek_gupta.SampleProject.prateek_gupta;

public interface SQS {
    String sendMessage(String queueName, String message) throws ServiceException;
    void pollMessages(String queueName);
    void updateQueueNames(String queueName, boolean isAdded) throws ServiceException;
}
