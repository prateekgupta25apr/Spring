package prateek_gupta.SampleProject.aws;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prateek_gupta.SampleProject.prateek_gupta.AWS;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/aws")
public class AWSController {

    @Autowired
    private AWS aws;

    @GetMapping("/get")
    public ResponseEntity<?> get(
            HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileName=request.getParameter("fileName");
            if (StringUtils.isNotBlank(fileName))
                if (aws.fileExists(fileName))
                    aws.getFile(fileName,response);
                else
                    return Util.getErrorResponse(new ServiceException(
                            HttpStatus.BAD_REQUEST,"File doesn't exists"));
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_DATA);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return Util.getSuccessResponse("");
    }

    @PostMapping("/upload")
    public ResponseEntity<ObjectNode> upload(@RequestParam("file") MultipartFile file) {
        ResponseEntity<ObjectNode> response;
        try {
            String fileName = aws.uploadFile(file);
            response = Util.getSuccessResponse("Successfully uploaded the file " + fileName);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectNode> delete(@RequestParam("fileName") String fileName) {
        ResponseEntity<ObjectNode> response;
        try {
            if (StringUtils.isNotBlank(fileName)) {
                aws.deleteFile(fileName);
                response = Util.getSuccessResponse("File deleted successfully");
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
