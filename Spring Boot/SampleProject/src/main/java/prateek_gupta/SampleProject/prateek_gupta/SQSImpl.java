package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
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

        if (credentialsProvider.resolveCredentials() != null)
            sqsClient = SqsClient.builder()
                    .region(Region.of(regionName))
                    .credentialsProvider(credentialsProvider)
                    .build();

        if (sqsClient == null)
            sqsClient = SqsClient.builder()
                    .region(Region.of(regionName))
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

                System.out.println("Received: " + msg.body());

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
}
