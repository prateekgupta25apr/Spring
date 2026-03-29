package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.commons.lang.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQSImpl implements SQS{

    SqsClient sqsClient;

    Map<String,String> queue_Name_URL_map;

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

        queue_Name_URL_map = new HashMap<>();
    }

    String getQueueUrl(String queueName) {
        String queueUrl = queue_Name_URL_map.get(queueName);

        if (StringUtils.isBlank(queueUrl)) {
            queueUrl=sqsClient.getQueueUrl(
                    GetQueueUrlRequest.builder()
                            .queueName(queueName)
                            .build()).queueUrl();
            queue_Name_URL_map.put(queueName,queueUrl);
        }

        return queueUrl;
    }

    @Override
    public String sendMessage(String queueName, String message) throws ServiceException {
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
    public void receiveMessage(String queueName) throws ServiceException {
        try{
            String queueUrl = getQueueUrl(queueName);

            ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(1)
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
            throw new ServiceException(e.getMessage());
        }
    }
}
