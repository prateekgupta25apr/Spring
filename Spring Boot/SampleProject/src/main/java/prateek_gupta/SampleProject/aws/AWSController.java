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
            ServiceException.moduleLockCheck("AWS_ENABLE",true);

            String fileName=request.getParameter("fileName");
            if (StringUtils.isNotBlank(fileName))
                if (aws.fileExists(fileName))
                    aws.getFile(fileName,response);
                else
                    return Util.getErrorResponse(new ServiceException(
                            HttpStatus.BAD_REQUEST,"File doesn't exists"));
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException e) {
            return Util.getErrorResponse(e);
        }
        return Util.getSuccessResponse("");
    }

    @PostMapping("/upload")
    public ResponseEntity<ObjectNode> upload(@RequestParam("file") MultipartFile file) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("AWS_ENABLE",true);

            String fileName=file.getOriginalFilename();
            String fileKey= aws.updateFileName(file.getOriginalFilename());
            fileKey= aws.uploadFile(file,fileKey);
            ObjectNode responseData=Util.getObjectMapper().createObjectNode();
            responseData.put("file_name",fileName);
            responseData.put("file_key",fileKey);
            responseData.put("pre_signed_url",
                    aws.generatePreSignedUrl(fileKey,"GET"));
            response = Util.getSuccessResponse(
                    "Successfully uploaded the file : " + fileName,
                    responseData);
        } catch (ServiceException e) {
            return Util.getErrorResponse(e);
        }
        return response;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectNode> delete(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("AWS_ENABLE",true);

            String fileName=request.getParameter("fileName");
            if (StringUtils.isNotBlank(fileName)) {
                aws.deleteFile(fileName);
                response = Util.getSuccessResponse("File deleted successfully");
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException e) {
            return Util.getErrorResponse(e);
        }
        return response;
    }

    @GetMapping("/get_pre_signed_url")
    public ResponseEntity<ObjectNode> getPreSignedUrl(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("AWS_ENABLE",true);

            String fileName=request.getParameter("fileName");
            String method=request.getParameter("method");
            if (StringUtils.isNotBlank(fileName))
                if (aws.fileExists(fileName)) {
                    if (StringUtils.isBlank(method))
                        method = "GET";
                    String url=aws.generatePreSignedUrl(fileName, method);
                    ObjectNode responseData=Util.getObjectMapper().createObjectNode();
                    responseData.put("method",method);
                    responseData.put("preSingedUrl",url);
                    response = Util.getSuccessResponse(
                            "File deleted successfully",responseData);
                }
                else
                    return Util.getErrorResponse(new ServiceException(
                            HttpStatus.BAD_REQUEST,"File doesn't exists"));
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException e) {
            return Util.getErrorResponse(e);
        }
        return response;
    }

    @PostMapping("/extract_file_name")
    public ResponseEntity<ObjectNode> extractFilename(HttpServletRequest request) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("AWS_ENABLE",true);

            String filePath=request.getParameter("file_path");
            if (StringUtils.isNotBlank(filePath)) {
                String fileName = aws.extractFileName(filePath, true);
                ObjectNode responseData = Util.getObjectMapper().createObjectNode();
                responseData.put("file_name", fileName);
                response = Util.getSuccessResponse(
                        "Extracted file name successfully", responseData);
            }
            else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException e) {
            return Util.getErrorResponse(e);
        }
        return response;
    }
}
