package prateek_gupta.SampleProject.prateek_gupta;

import java.util.List;

public interface Bedrock {
    List<Double> generateEmbeddingForImage(String imageUrl) throws ServiceException;
    List<Double> generateEmbeddingForText(String text) throws ServiceException;
}
