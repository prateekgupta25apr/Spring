package prateek_gupta.SampleProject.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import prateek_gupta.SampleProject.base.Context;
import prateek_gupta.SampleProject.prateek_gupta.S3;
import prateek_gupta.SampleProject.prateek_gupta.Email;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

@Component
public class Util {

    public static Object getClassObject(Method method)
            throws Exception {
        Object instance;
        try{
            instance= ApplicationContextSetter.context.getBean(method.getDeclaringClass());
        }catch (Exception e){
            instance=method.getDeclaringClass().getDeclaredConstructor().newInstance();
        }
        return instance;
    }

    public static ResponseEntity<ObjectNode>
    getSuccessResponse(String message, Object data){
        return getResponse(message,data,HttpStatus.OK);
    }

    public static ResponseEntity<ObjectNode>
    getSuccessResponse(String message){
        return getResponse(message,null,HttpStatus.OK);
    }

    public static ResponseEntity<ObjectNode> getErrorResponse(
            Exception exception){
        String message;
        HttpStatus status;
        if (exception instanceof ServiceException) {
            message=((ServiceException)exception).exceptionMessage;
            status=((ServiceException)exception).status;
        }
        else {
            message=exception.getMessage();
            status=HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return Util.getResponse(message,null,status);
    }

    public static ResponseEntity<ObjectNode> getResponse(
            String message,HttpStatus status){
        return getResponse(message,null,status);
    }

    public static ResponseEntity<ObjectNode> getResponse(
            String message, Object data,HttpStatus status){
        ObjectMapper objectMapper=getObjectMapper();
        ObjectNode responseJSON = objectMapper.createObjectNode();
        responseJSON.put("message", message);
        JsonNode dataNode=getObjectMapper().createObjectNode();
        if (data!=null)
            if (data instanceof String)
                try{
                    dataNode=objectMapper.readTree(String.valueOf(data));
                }catch (Exception e){
                    dataNode=objectMapper.valueToTree(data);
                }
            else
                dataNode=objectMapper.valueToTree(data);


        Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            responseJSON.set(field.getKey(),field.getValue());
        }
        return new ResponseEntity<>(responseJSON, status);
    }

    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper= new ObjectMapper();
        objectMapper.addMixIn(ParsedStringTerms.ParsedBucket.class,
                IgnoreParsedBucketMixin.class);
        return objectMapper;
    }

    public static JsonNode getJsonNode(Object data){
        JsonNode response ;
        if (data instanceof String)
            try{
                response= getObjectMapper().readTree(String.valueOf(data));
            }catch (Exception e){
                response= getObjectMapper().valueToTree(data);
            }
        else
            response= getObjectMapper().valueToTree(data);
        return response;
    }

    abstract static class IgnoreParsedBucketMixin {
        @JsonIgnore
        public abstract Number getKeyAsNumber();
    }

    public static void validateUserLogin() throws ServiceException {
        if (Context.getCurrentContext().userId<=0)
            throw new ServiceException(ServiceException.ExceptionType.LOGIN_REQUIRED);
    }

    @Autowired(required = false)
    JavaMailSender mailSender;

    @Autowired(required = false)
    Email email;

    @Autowired(required = false)
    S3 s3;

    public JSONArray sendEmail(
            String fromEmail, String toEmail, String subject,
            String content, JSONArray attachments) {

        JSONArray failedAttachments = new JSONArray();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String plainContent = email.getPlainContent(content);
            Object[] htmlContentAndInlineAttachments =
                    email.getHtmlContentAndInlineAttachments(content);
            String htmlContent = htmlContentAndInlineAttachments[0].toString();
            JSONArray inlineAttachment = (JSONArray) htmlContentAndInlineAttachments[1];

            helper.setText(plainContent, htmlContent);
            JSONObject attachment;

            // Processing Inline Attachments
            if (inlineAttachment != null) {
                for (Object attachmentObj : inlineAttachment) {
                    attachment=JSONObject.fromObject(attachmentObj.toString());
                    String fileUrl = attachment.has("file_url")?
                            attachment.getString("file_url"):"";
                    String fileName = attachment.getString("file_name");
                    String cid = attachment.getString("cid");
                    try {
                        byte[] fileContent=null;

                        // Fetching file based on file name
                        if (!fileUrl.contains("https://"))
                            fileContent= s3.getFileContentInBytes(fileUrl);
                            // Fetching file based on pre-signed url
                        else {
                            ResponseEntity<byte[]> response= new RestTemplate().exchange(
                                    fileUrl, HttpMethod.GET,
                                    null, byte[].class);

                            if (response.getStatusCodeValue()==200)
                                fileContent = response.getBody();
                        }

                        String contentType = s3.getFileDetails(fileName).contentType();

                        if (fileContent != null) {
                            DataSource dataSource =
                                    new ByteArrayDataSource(fileContent, contentType);

                            helper.addInline(cid,dataSource);

                        } else {
                            failedAttachments.add(fileName);
                        }
                    } catch (Exception e) {
                        failedAttachments.add(fileName);
                    }
                }
            }

            // Processing Normal Attachments
            if (attachments != null) {
                for (Object attachmentObj : attachments) {
                    attachment=JSONObject.fromObject(attachmentObj.toString());
                    String fileUrl = attachment.has("file_url")?
                            attachment.getString("file_url"):"";
                    String fileKey = attachment.has("file_key")?
                            attachment.getString("file_key"):"";
                    String fileName = attachment.getString("file_name");
                    try {
                        byte[] fileContent=null;

                        // Fetching file based on file name
                        if (StringUtils.isNotBlank(fileKey))
                            fileContent= s3.getFileContentInBytes(fileKey);
                            // Fetching file based on pre-signed url
                        else {
                            ResponseEntity<byte[]> response= new RestTemplate().exchange(
                                    fileUrl, HttpMethod.GET,
                                    null, byte[].class);

                            if (response.getStatusCodeValue()==200)
                                fileContent = response.getBody();
                        }

                        String contentType = s3.getFileDetails(fileName).contentType();

                        if (fileContent != null) {
                            DataSource dataSource =
                                    new ByteArrayDataSource(fileContent, contentType);

                            helper.addAttachment(fileName,dataSource);

                        } else {
                            failedAttachments.add(fileName);
                        }
                    } catch (Exception e) {
                        failedAttachments.add(fileName);
                    }
                }
            }
            mailSender.send(message);
        } catch (Exception e) {
            ServiceException.logException(e);
        }
        return failedAttachments;
    }
}
