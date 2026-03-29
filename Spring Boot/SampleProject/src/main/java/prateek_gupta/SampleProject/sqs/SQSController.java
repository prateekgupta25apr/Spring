package prateek_gupta.SampleProject.sqs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.prateek_gupta.SQS;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

@RestController
@RequestMapping("/sqs")
public class SQSController {

    @Autowired(required = false)
    SQS sqs;

    @PostMapping("send_message")
    ResponseEntity<ObjectNode> sendMessage(
            @RequestParam String queueName,@RequestParam String message) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);

            if (StringUtils.isNotBlank(queueName)&&StringUtils.isNotBlank(message)) {
                String messageId=sqs.sendMessage(queueName, message);
                JSONObject obj = new JSONObject();
                obj.put("messageId", messageId);
                response=Util.getSuccessResponse("Successfully sent message",obj);
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("receive_message")
    ResponseEntity<ObjectNode> receiveMessage(
            @RequestParam String queueName) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);

            if (StringUtils.isNotBlank(queueName)) {
                sqs.receiveMessage(queueName);
                response=Util.getSuccessResponse("Successfully received message");
            } else
                throw new ServiceException(
                        ServiceException.ExceptionType.MISSING_REQUIRED_PARAMETERS);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }
}
