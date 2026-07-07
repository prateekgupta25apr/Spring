package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BedrockImpl implements Bedrock {

    private final Logger log = LoggerFactory.getLogger(BedrockImpl.class);

    private BedrockRuntimeClient bedrockRuntimeClient;

    public BedrockImpl(String accessKey, String secretKey, String regionName)
            throws ServiceException {
        DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();
        Region region;
        if (StringUtils.isNotEmpty(regionName))
            region = Region.of(regionName);
        else
            region = DefaultAwsRegionProviderChain.builder().build().getRegion();

        if (credentialsProvider.resolveCredentials() != null)
            bedrockRuntimeClient = BedrockRuntimeClient.builder()
                    .credentialsProvider(credentialsProvider)
                    .region(region)
                    .build();

        bedrockRuntimeClient = BedrockRuntimeClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        if (bedrockRuntimeClient == null)
            throw new ServiceException("Error while creating an instance of Bedrock Client");
    }

    @Override
    public List<Double> generateEmbeddingForImage(String imageUrl) throws ServiceException {
        log.info("Entering generateEmbeddingForImage()");
        try {
            byte[] imageBytes = new RestTemplate().getForObject(imageUrl, byte[].class);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            JSONObject requestBody = new JSONObject();
            requestBody.put("inputImage", base64Image);

            InvokeModelResponse response = bedrockRuntimeClient.invokeModel(
                    InvokeModelRequest.builder()
                            .modelId("amazon.titan-embed-image-v1")
                            .contentType("application/json")
                            .accept("application/json")
                            .body(SdkBytes.fromUtf8String(requestBody.toString()))
                            .build());

            return parseEmbedding(response.body().asUtf8String());
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException("Error while generating image embedding");
        } finally {
            log.info("Exiting generateEmbeddingForImage()");
        }
    }

    @Override
    public List<Double> generateEmbeddingForText(String text) throws ServiceException {
        log.info("Entering generateEmbeddingForText()");
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("inputText", text);

            InvokeModelResponse response = bedrockRuntimeClient.invokeModel(
                    InvokeModelRequest.builder()
                            .modelId("amazon.titan-embed-text-v2:0")
                            .contentType("application/json")
                            .accept("application/json")
                            .body(SdkBytes.fromUtf8String(requestBody.toString()))
                            .build());

            return parseEmbedding(response.body().asUtf8String());
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException("Error while generating text embedding");
        } finally {
            log.info("Exiting generateEmbeddingForText()");
        }
    }

    private List<Double> parseEmbedding(String responseBody) {
        JSONArray embeddingArray = JSONObject.fromObject(responseBody).getJSONArray("embedding");
        List<Double> embedding = new ArrayList<>();
        for (int i = 0; i < embeddingArray.size(); i++)
            embedding.add(embeddingArray.getDouble(i));
        return embedding;
    }
}
