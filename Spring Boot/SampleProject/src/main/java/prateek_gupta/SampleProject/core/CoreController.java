package prateek_gupta.SampleProject.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CoreController {

    private static final Logger logger = LogManager.getLogger(
            CoreController.class);

    @Autowired
    CoreService coreService;

    @GetMapping("test")
    ResponseEntity<ObjectNode> test(@RequestParam String testData) {
        logger.info("Entering test() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            ObjectNode data = coreService.test(testData);
            response = Util.getSuccessResponse("Success", data);

        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        logger.info("Exiting test() Controller");
        return response;
    }

    @PostMapping("rotate_log_files")
    ResponseEntity<ObjectNode> rotateLogFiles(HttpServletRequest request) {
        logger.info("Entering rotateLogFiles() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            String daysGapStr=request.getParameter("days_gap");
            int daysGap;
            if (StringUtils.isNotBlank(daysGapStr))
                daysGap=Integer.parseInt(daysGapStr);
            else
                daysGap= prateek_gupta.SampleProject.prateek_gupta.LogManager.
                        DAYS_GAP_FOR_ROTATION;
            prateek_gupta.SampleProject.prateek_gupta.LogManager.rotateLogFiles(daysGap);
            response = Util.getSuccessResponse("Successfully deleted the log files");

        } catch (ServiceException exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        logger.info("Exiting rotateLogFiles() Controller");
        return response;
    }
}
