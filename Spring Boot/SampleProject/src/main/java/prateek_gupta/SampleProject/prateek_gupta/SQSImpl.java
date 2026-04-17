package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SQSImpl implements SQS{

    private final Logger log = LoggerFactory.getLogger(SQSImpl.class);

    SqsClient sqsClient;

    Map<String,String> QueueName_QueueURL_Map;

    Map<String, ScheduledFuture<?>> QueueName_ScheduledConsumer_Map =
            new HashMap<>();

    static List<String> queueNames=new ArrayList<>();

    ScheduledExecutorService scheduledExecutorService=
            Executors.newScheduledThreadPool(5);

    public SQSImpl(
            String accessKey, String secretKey, String regionName) {
        DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();
        Region region;
        if (StringUtils.isNotEmpty(regionName))
            region = Region.of(regionName);
        else
            region = DefaultAwsRegionProviderChain.builder().build().getRegion();

        if (credentialsProvider.resolveCredentials() != null)
            sqsClient = SqsClient.builder()
                    .region(region)
                    .credentialsProvider(credentialsProvider)
                    .build();

        if (sqsClient == null)
            sqsClient = SqsClient.builder()
                    .region(region)
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();

        QueueName_QueueURL_Map = new HashMap<>();

        for(String queueName:queueNames)
            scheduleMessagesPolling(queueName,true);
    }

    String getQueueUrl(String queueName) {
        String queueUrl = QueueName_QueueURL_Map.get(queueName);

        if (StringUtils.isBlank(queueUrl)) {
            queueUrl=sqsClient.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName(queueName)
                            .build()).queueUrl();
            QueueName_QueueURL_Map.put(queueName,queueUrl);
        }

        return queueUrl;
    }

    public boolean isQueueExist(String queueName) {
        boolean result;
        try{
            String queueUrl = getQueueUrl(queueName);
            result=StringUtils.isNotBlank(queueUrl);
        }catch (Exception e){
            result=false;
        }
        return result;
    }

    @Override
    public String sendMessage(
            String queueName, String message) throws ServiceException {
        String messageId;
        try{
            String queueUrl = getQueueUrl(queueName);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();

            SendMessageResponse response= sqsClient.sendMessage(request);
            messageId=response.messageId();

        }catch(Exception e){
            ServiceException.logException(e);
            throw new ServiceException(e.getMessage());
        }
        return messageId;
    }

    @Override
    public void pollMessages(String queueName) {
        try{
            String queueUrl = getQueueUrl(queueName);

            ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .waitTimeSeconds(20)
                    .build();

            ReceiveMessageResponse response = sqsClient.receiveMessage(request);

            List<Message> messages = response.messages();

            for (Message msg : messages) {

                System.out.println("Received message :: " + msg.body());

                // delete message after processing
                DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(msg.receiptHandle())
                        .build();

                sqsClient.deleteMessage(deleteRequest);
            }


        }catch(Exception e){
            ServiceException.logException(e);
        }
    }

    void scheduleMessagesPolling(
            String queueName, boolean isAdded) {
        log.info("Entering Service : scheduleMessagesPolling()");

        if (isAdded) {
            ScheduledFuture<?> scheduledFuture =
                    scheduledExecutorService.scheduleWithFixedDelay(
                    ()->pollMessages(queueName),0,1, TimeUnit.SECONDS
            );
            QueueName_ScheduledConsumer_Map.put(queueName,scheduledFuture);
        }
        else {
            ScheduledFuture<?> scheduledConsumer =
                    QueueName_ScheduledConsumer_Map.get(queueName);
            scheduledConsumer.cancel(true);
            QueueName_ScheduledConsumer_Map.remove(queueName);
        }
        log.info("Exiting Service : scheduleMessagesPolling()");
    }

    @Override
    public void updateQueueNames(String queueName, boolean isAdded) {
        if (isAdded){
            queueNames.add(queueName);
            scheduleMessagesPolling(queueName,true);
        }
        else {
            queueNames.remove(queueName);
            scheduleMessagesPolling(queueName,false);
        }
    }

    @Override
    public List<String> getAllQueues() throws ServiceException {
        List<String> queues = new ArrayList<>();
        try{
            ListQueuesResponse response = sqsClient.listQueues(
                    ListQueuesRequest.builder().build()
            );

            for (String queueUrl : response.queueUrls()) {
                // Extract name from URL
                String queueName = queueUrl.substring(
                        queueUrl.lastIndexOf("/") + 1);
                queues.add(queueName);
            }
        }catch(Exception e){
            ServiceException.logException(e);
            throw new ServiceException(e.getMessage());
        }
        return queues;
    }

    @Override
    public JSONObject getQueue(String queueName) throws ServiceException {
        JSONObject queue=new JSONObject();
        try{
            String queueUrl = getQueueUrl(queueName);
            GetQueueAttributesRequest request = GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNames(QueueAttributeName.ALL)
                    .build();

            GetQueueAttributesResponse response = sqsClient.getQueueAttributes(request);

            queue.putAll(response.attributes());
        }catch(Exception e){
            ServiceException.logException(e);
            throw new ServiceException(e.getMessage());
        }
        return queue;
    }

    @Override
    public void createQueue(
            String queueName,String visibilityTimeOut,String retentionPeriod)
            throws ServiceException {
        try{
            if (StringUtils.isBlank(visibilityTimeOut))
                visibilityTimeOut="30";

            if (StringUtils.isBlank(retentionPeriod))
                retentionPeriod="86400"; // 1 day

            Map<QueueAttributeName, String> attributes = new HashMap<>();
            attributes.put(QueueAttributeName.VISIBILITY_TIMEOUT, visibilityTimeOut);
            attributes.put(QueueAttributeName.MESSAGE_RETENTION_PERIOD, retentionPeriod);

            CreateQueueRequest request = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .attributes(attributes)
                    .build();

            CreateQueueResponse response = sqsClient.createQueue(request);

            String queueUrl = response.queueUrl();
            QueueName_QueueURL_Map.put(queueName,queueUrl);
        }catch(Exception e){
            ServiceException.logException(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void updateQueue(String queueName, String attributeName, String attributeValue)
            throws ServiceException {
        try{
            String queueUrl = getQueueUrl(queueName);
            Map<QueueAttributeName, String> attributes = new HashMap<>();
            attributes.put(QueueAttributeName.valueOf(attributeName), attributeValue);

            SetQueueAttributesRequest request = SetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributes(attributes)
                    .build();

            sqsClient.setQueueAttributes(request);
        }catch(Exception e){
            ServiceException.logException(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteQueue(String queueName) throws ServiceException {
        try{
            String queueUrl = getQueueUrl(queueName);

            DeleteQueueRequest request = DeleteQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build();

            sqsClient.deleteQueue(request);

            QueueName_QueueURL_Map.remove(queueName);
        }catch(Exception e){
            ServiceException.logException(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public JSONArray getMessages(String queueName) throws ServiceException {
        JSONArray messages=new JSONArray();
        try{
            String queueUrl = getQueueUrl(queueName);

            ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(20)
                    .build();

            ReceiveMessageResponse response = sqsClient.receiveMessage(request);
            List<Message> responseMessages = response.messages();
            JSONObject message;

            while(!responseMessages.isEmpty()){
                for (Message msg : responseMessages) {
                    message=new JSONObject();
                    message.put("message_id",msg.messageId());
                    message.put("body", msg.body());
                    messages.add(message);
                }

                response = sqsClient.receiveMessage(request);
                responseMessages = response.messages();
            }


        }catch(Exception e){
            ServiceException.logException(e);
            throw new ServiceException(e.getMessage());
        }
        return messages;
    }
}
