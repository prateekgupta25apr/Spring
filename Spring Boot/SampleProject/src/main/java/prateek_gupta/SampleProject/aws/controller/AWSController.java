package prateek_gupta.SampleProject.aws.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prateek_gupta.SampleProject.aws.service.AWSService;
import prateek_gupta.SampleProject.base.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

@RestController
@RequestMapping("/aws")
public class AWSController {
    @Autowired
    private AWSService awsService;

    @PostMapping("/upload")
    public ResponseEntity<ObjectNode> uploadFile(@RequestParam("file") MultipartFile file) {
        ResponseEntity<ObjectNode> response;
        try {
            String eTag = awsService.uploadFile(file);
            response = Util.getSuccessResponse("File uploaded successfully. ETag: " + eTag);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectNode> deleteFile(@RequestParam("fileName") String fileName) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(fileName)) {
                String eTag = awsService.deleteFile(fileName);
                response = Util.getSuccessResponse("File deleted successfully. ETag "+eTag);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }
}
