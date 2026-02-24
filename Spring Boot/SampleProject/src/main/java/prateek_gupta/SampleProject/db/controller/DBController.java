package prateek_gupta.SampleProject.db.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prateek_gupta.SampleProject.prateek_gupta.AWS;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;
import prateek_gupta.SampleProject.db.service.DBService;
import prateek_gupta.SampleProject.db.vo.Table1VO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("db")
public class DBController {

    private final Logger log = LoggerFactory.getLogger(DBController.class);
    @Autowired
    DBService dbService;

    @Autowired
    AWS aws;

    @GetMapping("get_data")
    ResponseEntity<ObjectNode> getData(@RequestParam Integer primaryKey) {
        ResponseEntity<ObjectNode> response;
        try {
            if (primaryKey != null && primaryKey > 0) {
                Table1VO table1VO = dbService.getData(primaryKey);
                if (table1VO != null) {
                    JSONObject data = new JSONObject();
                    data.put("table1", table1VO);
                    response = Util.getSuccessResponse(
                            "Successfully fetched the data", data);
                } else
                    throw new ServiceException(
                            ServiceException.ExceptionType.DB_ERROR);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        return response;
    }

    @PostMapping("save_data")
    ResponseEntity<ObjectNode> saveData(@RequestBody Object data) {
        log.info("Entered Controller : saveData()");
        ResponseEntity<ObjectNode> response;
        try {
            if (data != null) {
                dbService.saveData(data);
                response = Util.getSuccessResponse("Successfully saved the data");
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        log.info("Exiting Controller : saveData()");
        return response;
    }

    @PutMapping("update_data")
    ResponseEntity<ObjectNode> updateData(HttpServletRequest request) {
        log.info("Entered Controller : updateData()");
        ResponseEntity<ObjectNode> response;
        try {
            String primaryKeyStr=request.getParameter("primaryKey");
            String col1Str=request.getParameter("col1");
            String col2Str=request.getParameter("col2");

            if (StringUtils.isBlank(primaryKeyStr) ||
                    StringUtils.isBlank(col1Str) ||
                    StringUtils.isBlank(col2Str))
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);

            int primaryKey;
            String col1;
            boolean col2;

            try{
                primaryKey=Integer.parseInt(primaryKeyStr);
                if(primaryKey<=0)
                    throw new ServiceException(
                            ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);

                col1=col1Str;

                col2=Boolean.parseBoolean(col2Str);
            }
            catch (Exception exception){
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
            }

            dbService.updateData(primaryKey,col1,col2);
            response = Util.getSuccessResponse("Successfully updated the data");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        log.info("Exiting Controller : updateData()");
        return response;
    }

    @PatchMapping("partial_update_data")
    ResponseEntity<ObjectNode> partialUpdateData(HttpServletRequest request) {
        log.info("Entered Controller : partialUpdateData()");
        ResponseEntity<ObjectNode> response;
        try {
            String primaryKeyStr=request.getParameter("primaryKey");
            String col1Str=request.getParameter("col1");
            String col2Str=request.getParameter("col2");

            if (StringUtils.isBlank(primaryKeyStr) ||
                    (StringUtils.isBlank(col1Str) &&
                    StringUtils.isBlank(col2Str)))
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);

            int primaryKey;
            String col1;
            Boolean col2;

            try{
                primaryKey=Integer.parseInt(primaryKeyStr);
                if(primaryKey<=0)
                    throw new ServiceException(
                            ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);

                col1=col1Str;

                if (StringUtils.isNotBlank(col2Str))
                    col2=Boolean.parseBoolean(col2Str);
                else
                    col2=null;
            }
            catch (Exception exception){
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
            }

            dbService.partialUpdateData(primaryKey,col1,col2);
            response = Util.getSuccessResponse("Successfully updated the data");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        log.info("Exiting Controller : partialUpdateData()");
        return response;
    }

    @DeleteMapping("delete_data")
    ResponseEntity<ObjectNode> deleteData(@RequestParam Integer primaryKey) {
        ResponseEntity<ObjectNode> response;
        try {
            if (primaryKey != null && primaryKey > 0) {
                dbService.deleteData(primaryKey);
                response = Util.getSuccessResponse("Successfully deleted the data");
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        return response;
    }

    @PostMapping("add_attachment")
    ResponseEntity<ObjectNode> addAttachment(
            @RequestParam("table_1_primary_key") Integer table1PrimaryKey,
            @RequestParam("attachment") MultipartFile attachment
        ) {
        ResponseEntity<ObjectNode> response;
        try {
            String fileKey = "Table1AttachmentMapping/"+table1PrimaryKey+"/"+
                    attachment.getOriginalFilename();
            aws.uploadFile(attachment, fileKey);
            dbService.addAttachment(table1PrimaryKey, fileKey);
            response = Util.getSuccessResponse("Attachment added successfully");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        return response;
    }

    @GetMapping("get_attachment")
    ResponseEntity<ObjectNode> getAttachment(
            @RequestParam Integer primaryKey,
            HttpServletResponse servletResponse
    ) {
        ResponseEntity<ObjectNode> response;
        try {
            String fileKey = dbService.getAttachmentPath(primaryKey);
            aws.getFile(fileKey, servletResponse);
            response = Util.getSuccessResponse("Attachment added successfully");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        return response;
    }

}
