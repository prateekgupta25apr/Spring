package prateek_gupta.SampleProject.email;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.prateek_gupta.Email;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

@RestController
@RequestMapping("emails")
public class EmailController {
    private final Logger log = LoggerFactory.getLogger(EmailController.class);

    @Autowired(required = false)
    Email email;

    @Autowired(required = false)
    Util util;


    @GetMapping("/get_email_content")
    ResponseEntity<ObjectNode> getEmailContent(
            @RequestParam(value = "message_id",required = false) String messageId,
            @RequestParam(value = "file_path",required = false) String filePath,
            @RequestParam(value = "fetch_file_url",required = false) boolean fetchFileUrl
    ) {
        log.info("Entered Controller : getEmailContent()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("EMAILS_ENABLED", true);
            JSONObject emailContent;

            emailContent= email.getEmailContent(
                        messageId, filePath, fetchFileUrl);

            response = Util.getSuccessResponse("Successfully fetched email data",
                    emailContent);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        log.info("Exiting Controller : getEmailContent()");
        return response;
    }


    @PostMapping("/send_email")
    ResponseEntity<ObjectNode> send(
            @RequestParam("from_email") String fromEmail,
            @RequestParam("to_email") String toEmail,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam("attachments") String attachments,
            @RequestParam(value = "native", required = false) boolean nativeEnabled
    ) {
        log.info("Entered Controller : send()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("EMAILS_ENABLED", true);
            JSONArray failedAttachments;

            if (nativeEnabled)
                failedAttachments = util.sendEmail(
                        fromEmail, toEmail, subject, content, JSONArray.fromObject(attachments));
            else
                failedAttachments = email.send(
                        fromEmail, toEmail, subject, content, JSONArray.fromObject(attachments));
            JSONObject data = new JSONObject();
            data.put("failedAttachments", failedAttachments);

            response = Util.getSuccessResponse("Successfully sent email", data);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        log.info("Exiting Controller : send()");
        return response;
    }

    @PostMapping("/process_email")
    ResponseEntity<ObjectNode> processEmail(
            @RequestParam(value = "message_id",required = false) String messageId,
            @RequestParam(value = "file_path",required = false) String filePath,
            @RequestParam(value = "to_email",required = false) String toEmail
    ) {
        log.info("Entered Controller : processEmail()");
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("EMAILS_ENABLED", true);

            email.processEmail(messageId, filePath, toEmail);

            response = Util.getSuccessResponse("Successfully processed email");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        log.info("Exiting Controller : processEmail()");
        return response;
    }

}
