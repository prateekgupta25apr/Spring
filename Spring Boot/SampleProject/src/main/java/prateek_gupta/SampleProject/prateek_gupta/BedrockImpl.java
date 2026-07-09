package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.http.auth.aws.signer.AwsV4HttpSigner;
import software.amazon.awssdk.http.auth.spi.signer.SignedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BedrockImpl implements Bedrock {

    private final Logger log = LoggerFactory.getLogger(BedrockImpl.class);

    private BedrockRuntimeClient bedrockRuntimeClient;

    private final String accessKey;

    private final String secretKey;

    private final String regionName;

    private final Cryptography cryptography;

    public BedrockImpl(
            String accessKey, String secretKey, String regionName, Cryptography cryptography)
            throws ServiceException {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.regionName = regionName;
        this.cryptography = cryptography;

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

    @Override
    public Map<String, Object> generateSignedHeadersManually(
            String url, String payload, String requestMethod, String requestContentType,
            String serviceName, boolean apiCall) throws ServiceException {
        log.info("Entering generateSignedHeadersManually()");
        try {
            validateAwsCredentials();
            RequestContext context = prepareRequestContext(
                    url, payload, requestMethod, requestContentType);

            List<String> sortedSignedHeaders = new ArrayList<>(context.signedHeaderNames);
            sortedSignedHeaders.sort(String::compareTo);

            StringBuilder canonicalStr = new StringBuilder();
            canonicalStr.append(context.requestMethod).append('\n');
            canonicalStr.append(encodePath(URI.create(url).getPath())).append('\n');
            canonicalStr.append('\n');

            for (String header : sortedSignedHeaders)
                canonicalStr.append(header).append(':')
                        .append(context.headersConfig.get(header)).append('\n');

            canonicalStr.append('\n');
            canonicalStr.append(String.join(";", sortedSignedHeaders)).append('\n');
            canonicalStr.append(context.payloadHash);

            String canonicalHash = cryptography.hashSHA256(canonicalStr.toString());
            String stringToSign = "AWS4-HMAC-SHA256\n" + context.timeVal + '\n'
                    + context.dateVal + '/' + regionName + '/' + serviceName + "/aws4_request\n"
                    + canonicalHash;

            String signature = cryptography.hMacSHA256Hex(
                    getSigningKey(secretKey, context.dateVal, regionName, serviceName),
                    stringToSign);

            Map<String, String> signedHeadersResponse = new LinkedHashMap<>();
            for (String header : sortedSignedHeaders)
                signedHeadersResponse.put(header, context.headersConfig.get(header));
            signedHeadersResponse.put("X-Amz-Content-Sha256", context.payloadHash);
            signedHeadersResponse.put("X-Amz-Date", context.timeVal);
            signedHeadersResponse.remove("x-amz-content-sha256");
            signedHeadersResponse.remove("x-amz-date");
            signedHeadersResponse.put("Authorization",
                    "AWS4-HMAC-SHA256 Credential=" + accessKey + '/' + context.dateVal + '/'
                            + regionName + '/' + serviceName + "/aws4_request,"
                            + "SignedHeaders=" + String.join(";", sortedSignedHeaders) + ","
                            + "Signature=" + signature);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("signed_headers", signedHeadersResponse);
            if (apiCall)
                result.put("api_response", executeApiCall(
                        url, signedHeadersResponse, context.payload, context.requestMethod));
            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException("Error while generating signed headers manually");
        } finally {
            log.info("Exiting generateSignedHeadersManually()");
        }
    }

    @Override
    public Map<String, Object> generateSignedHeadersUsingBuiltIn(
            String url, String payload, String requestMethod, String requestContentType,
            String serviceName, boolean apiCall) throws ServiceException {
        log.info("Entering generateSignedHeadersUsingBuiltIn()");
        try {
            validateAwsCredentials();
            RequestContext context = prepareRequestContext(
                    url, payload, requestMethod, requestContentType);

            List<String> sortedSignedHeaders = new ArrayList<>(context.signedHeaderNames);
            sortedSignedHeaders.sort(String::compareTo);

            SdkHttpRequest.Builder requestBuilder = SdkHttpRequest.builder()
                    .uri(URI.create(url))
                    .method(SdkHttpMethod.fromValue(context.requestMethod));

            for (String header : sortedSignedHeaders)
                requestBuilder.putHeader(header, context.headersConfig.get(header));

            SdkHttpRequest httpRequest = requestBuilder.build();
            ContentStreamProvider requestPayload = ContentStreamProvider.fromByteArray(
                    context.payload.getBytes(StandardCharsets.UTF_8));

            SignedRequest signedRequest = AwsV4HttpSigner.create()
                    .sign(r -> r
                    .identity(AwsBasicCredentials.create(accessKey, secretKey))
                    .request(httpRequest)
                    .payload(requestPayload)
                    .putProperty(AwsV4HttpSigner.SERVICE_SIGNING_NAME, serviceName)
                    .putProperty(AwsV4HttpSigner.REGION_NAME, regionName));

            Map<String, String> signedHeadersResponse = new LinkedHashMap<>();
            signedRequest.request().headers().forEach((key, values) -> {
                if (!values.isEmpty())
                    signedHeadersResponse.put(key, values.getFirst());
            });

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("signed_headers", signedHeadersResponse);
            if (apiCall)
                result.put("api_response", executeApiCall(
                        url, signedHeadersResponse, context.payload, context.requestMethod));
            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            ServiceException.logException(e);
            throw new ServiceException("Error while generating signed headers using built-in");
        } finally {
            log.info("Exiting generateSignedHeadersUsingBuiltIn()");
        }
    }

    private void validateAwsCredentials() throws ServiceException {
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey)
                || StringUtils.isBlank(regionName))
            throw new ServiceException("Couldn't establish a connection to AWS");
    }

    private RequestContext prepareRequestContext(
            String url, String payload, String requestMethod, String requestContentType)
            throws ServiceException {
        if (StringUtils.isBlank(requestMethod))
            requestMethod = "POST";
        if (StringUtils.isBlank(requestContentType))
            requestContentType = "application/json";

        Instant now = Instant.now();
        String timeVal = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(ZoneOffset.UTC).format(now);
        String dateVal = DateTimeFormatter.ofPattern("yyyyMMdd")
                .withZone(ZoneOffset.UTC).format(now);
        String payloadHash = cryptography.hashSHA256(payload);

        Map<String, String> headersConfig = new LinkedHashMap<>();
        headersConfig.put("accept", "application/json");
        headersConfig.put("host", URI.create(url).getHost());
        headersConfig.put("content-length",
                String.valueOf(payload.getBytes(StandardCharsets.UTF_8).length));
        headersConfig.put("content-type", requestContentType);
        headersConfig.put("x-amz-content-sha256", payloadHash);
        headersConfig.put("x-amz-date", timeVal);

        List<String> signedHeaderNames = List.of(
                "accept", "host", "content-length", "content-type",
                "x-amz-content-sha256", "x-amz-date");

        RequestContext context = new RequestContext();
        context.payload = payload;
        context.timeVal = timeVal;
        context.dateVal = dateVal;
        context.payloadHash = payloadHash;
        context.headersConfig = headersConfig;
        context.signedHeaderNames = signedHeaderNames;
        context.requestContentType = requestContentType;
        context.requestMethod = requestMethod;
        return context;
    }

    private Object executeApiCall(
            String url, Map<String, String> headers, String payload, String requestMethod) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::set);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(payload, httpHeaders);
        HttpMethod method = "GET".equalsIgnoreCase(requestMethod)
                ? HttpMethod.GET : HttpMethod.POST;
        ResponseEntity<String> response = restTemplate.exchange(
                url, method, entity, String.class);

        try {
            return JSONObject.fromObject(response.getBody());
        } catch (Exception e) {
            JSONObject fallback = new JSONObject();
            fallback.put("status_code", response.getStatusCode().value());
            fallback.put("text", response.getBody());
            return fallback;
        }
    }

    /**
     * URI-encodes each path segment for the SigV4 canonical request.
     * <br><br>
     * AWS requires the canonical URI to encode special characters per segment while
     * keeping {@code /} as the path separator. Bedrock model paths contain {@code :}
     * (for example {@code /model/amazon.titan-embed-text-v2:0/invoke}), which must
     * become {@code %3A} in the canonical string or the manual signature will not
     * match what AWS expects.
     * <br><br>
     * This mirrors Python's {@code urllib.parse.quote(urlparse(url).path)} used in
     * {@code generate_signed_headers_manually}. The built-in {@code AwsV4HttpSigner}
     * path encodes the path itself, so this helper is only needed for manual signing.
     */
    private String encodePath(String path) {
        String[] segments = path.split("/");
        StringBuilder encodedPath = new StringBuilder();
        for (String segment : segments) {
            if (segment.isEmpty())
                continue;
            encodedPath.append('/')
                    .append(URLEncoder.encode(segment, StandardCharsets.UTF_8)
                            .replace("+", "%20"));
        }
        if (encodedPath.isEmpty())
            encodedPath.append('/');
        return encodedPath.toString();
    }

    private byte[] getSigningKey(
            String argSecretKey, String argDate, String argRegion, String argService)
            throws ServiceException {
        byte[] kDate = cryptography.hMacSHA256Digest(
                ("AWS4" + argSecretKey).getBytes(StandardCharsets.UTF_8), argDate);
        byte[] kRegion = cryptography.hMacSHA256Digest(kDate, argRegion);
        byte[] kService = cryptography.hMacSHA256Digest(kRegion, argService);
        return cryptography.hMacSHA256Digest(kService, "aws4_request");
    }

    private List<Double> parseEmbedding(String responseBody) {
        JSONArray embeddingArray = JSONObject.fromObject(responseBody).
                getJSONArray("embedding");
        List<Double> embedding = new ArrayList<>();
        for (int i = 0; i < embeddingArray.size(); i++)
            embedding.add(embeddingArray.getDouble(i));
        return embedding;
    }

    private static class RequestContext {
        String payload;
        String timeVal;
        String dateVal;
        String payloadHash;
        Map<String, String> headersConfig;
        List<String> signedHeaderNames;
        String requestContentType;
        String requestMethod;
    }
}
