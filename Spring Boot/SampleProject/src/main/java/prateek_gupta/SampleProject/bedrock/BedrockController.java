package prateek_gupta.SampleProject.bedrock;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prateek_gupta.SampleProject.prateek_gupta.Bedrock;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.project_utils.Init;

import java.util.List;

@RestController
@RequestMapping("/bedrock")
public class BedrockController {

    @Autowired
    private Bedrock bedrock;

    @PostMapping("/generate_embedding_for_image")
    public ResponseEntity<ObjectNode> generateEmbeddingForImage(
            @RequestParam("image_url") String imageUrl) {
        try {
            ServiceException.moduleLockCheck("BEDROCK_ENABLE", true);

            if (StringUtils.isBlank(imageUrl))
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);

            List<Double> embedding = bedrock.generateEmbeddingForImage(imageUrl);
            ObjectNode responseData = Init.getObjectMapper().createObjectNode();
            responseData.put("message", "Image embedding generated successfully");
            responseData.set("embedding", Init.getObjectMapper().valueToTree(embedding));
            responseData.put("embedding_dimension", embedding.size());
            return Init.getSuccessResponse(responseData);
        } catch (ServiceException e) {
            return Init.getErrorResponse(e);
        } catch (Exception e) {
            return Init.getErrorResponse(new ServiceException());
        }
    }

    @PostMapping("/generate_embedding_for_text")
    public ResponseEntity<ObjectNode> generateEmbeddingForText(
            @RequestParam("text") String text) {
        try {
            ServiceException.moduleLockCheck("BEDROCK_ENABLE", true);

            if (StringUtils.isBlank(text))
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);

            List<Double> embedding = bedrock.generateEmbeddingForText(text);
            ObjectNode responseData = Init.getObjectMapper().createObjectNode();
            responseData.put("message", "Text embedding generated successfully");
            responseData.set("embedding", Init.getObjectMapper().valueToTree(embedding));
            responseData.put("embedding_dimension", embedding.size());
            return Init.getSuccessResponse(responseData);
        } catch (ServiceException e) {
            return Init.getErrorResponse(e);
        } catch (Exception e) {
            return Init.getErrorResponse(new ServiceException());
        }
    }
}
