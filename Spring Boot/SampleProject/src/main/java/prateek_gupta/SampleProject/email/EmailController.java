package prateek_gupta.SampleProject.email;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prateek_gupta.SampleProject.prateek_gupta.Email;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

@RestController
@RequestMapping("emails")
public class EmailController {
    private final Logger log = LoggerFactory.getLogger(EmailController.class);

    @Autowired(required = false)
    Email email;


    @PostMapping("/send_email")
    ResponseEntity<ObjectNode> send(
            @RequestParam("from_email") String fromEmail,
            @RequestParam("to_email") String toEmail,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam("attachments") String attachments,
            @RequestParam(value = "native",required = false) boolean nativeEnabled
    ) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("EMAILS_ENABLED", true);
                JSONArray failedAttachments =email.send(
                        fromEmail, toEmail, subject, content, JSONArray.fromObject(attachments));
                JSONObject data = new JSONObject();
                data.put("failedAttachments", failedAttachments);

                response = Util.getSuccessResponse("Successfully sent email",data);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        return response;
    }

}
