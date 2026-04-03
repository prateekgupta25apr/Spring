package prateek_gupta.SampleProject.sqs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.prateek_gupta.SQS;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

import java.util.List;

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

    @PostMapping("add_queue_name")
    ResponseEntity<ObjectNode> addQueueName(
            @RequestParam String queueName) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);

            if (StringUtils.isNotBlank(queueName)) {
                sqs.updateQueueNames(queueName, true);
                response=Util.getSuccessResponse("Successfully added queue name");
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

    @PostMapping("remove_queue_name")
    ResponseEntity<ObjectNode> removeQueueName(
            @RequestParam String queueName) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);

            if (StringUtils.isNotBlank(queueName)) {
                sqs.updateQueueNames(queueName, false);
                response=Util.getSuccessResponse("Successfully removed queue name");
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

    @GetMapping("get_all_queues")
    ResponseEntity<ObjectNode> getAllQueues() {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);
                List<String> queues=sqs.getAllQueues();
                JSONObject data = new JSONObject();
                data.put("queues", queues);
                response=Util.getSuccessResponse("Successfully removed queue name",data);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @GetMapping("get_queue")
    ResponseEntity<ObjectNode> getQueue(@RequestParam("queue_name") String queueName) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);
            JSONObject queue=sqs.getQueue(queueName);
            JSONObject data = new JSONObject();
            data.put("queue", queue);
            response=Util.getSuccessResponse("Successfully queue details",data);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @PostMapping("create_queue")
    ResponseEntity<ObjectNode> createQueue(
            @RequestParam("queue_name") String queueName,
            @RequestParam(value = "visibility_timeout",required = false) String visibilityTimeOut,
            @RequestParam(value = "retention_period",required = false) String retentionPeriod) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);
            sqs.createQueue(queueName,visibilityTimeOut,retentionPeriod);
            response=Util.getSuccessResponse("Successfully created queue");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }


    @PutMapping("update_queue")
    ResponseEntity<ObjectNode> updateQueue(
            @RequestParam("queue_name") String queueName,
            @RequestParam(value = "attribute_name") String attributeName,
            @RequestParam(value = "attribute_value") String attributeValue) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);
            sqs.updateQueue(queueName,attributeName,attributeValue);
            response=Util.getSuccessResponse("Successfully updated queue");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @DeleteMapping("delete_queue")
    ResponseEntity<ObjectNode> deleteQueue(@RequestParam("queue_name") String queueName) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);
            sqs.deleteQueue(queueName);
            response=Util.getSuccessResponse("Successfully queue delete");
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }

    @GetMapping("get_messages")
    ResponseEntity<ObjectNode> getMessages(@RequestParam("queue_name") String queueName) {
        ResponseEntity<ObjectNode> response;
        try {
            ServiceException.moduleLockCheck("SQS_ENABLE", true);
            JSONArray messages=sqs.getMessages(queueName);
            JSONObject data = new JSONObject();
            data.put("count", messages.size());
            data.put("messages", messages);
            response=Util.getSuccessResponse("Successfully fetched messages",data);
        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        return response;
    }
}
