package prateek_gupta.SampleProject.prateek_gupta;

import java.util.List;
import java.util.Map;

public interface Bedrock {
    List<Double> generateEmbeddingForImage(String imageUrl) throws ServiceException;
    List<Double> generateEmbeddingForText(String text) throws ServiceException;
    Map<String, Object> generateSignedHeadersManually(
            String url, String payload, String requestMethod, String requestContentType,
            String serviceName, boolean apiCall) throws ServiceException;
    Map<String, Object> generateSignedHeadersUsingBuiltIn(
            String url, String payload, String requestMethod, String requestContentType,
            String serviceName, boolean apiCall) throws ServiceException;
}
