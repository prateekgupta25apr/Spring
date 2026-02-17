package prateek_gupta.SampleProject.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import prateek_gupta.SampleProject.prateek_gupta.Init;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;
import prateek_gupta.SampleProject.utils.Util;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CoreController {

    private static final Logger logger = LogManager.getLogger(
            CoreController.class);

    @Autowired
    CoreService coreService;

    @Autowired
    HealthEndpoint healthEndpoint;

    @GetMapping("test")
    ResponseEntity<ObjectNode> test(@RequestParam String testData) {
        logger.info("Entering test() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            ObjectNode data = coreService.test(testData);
            response = Util.getSuccessResponse("Success", data);

        } catch (ServiceException exception) {
            return Util.getErrorResponse(exception);
        }
        logger.info("Exiting test() Controller");
        return response;
    }

    @GetMapping("health_check")
    ResponseEntity<ObjectNode> healthCheck() {
        logger.info("Entering healthCheck() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            if (healthEndpoint.health().getStatus().toString().equals("UP"))
                response = Util.getResponse("Healthy",HttpStatus.OK);
            else
                response = Util.getResponse("Unhealthy",HttpStatus.OK);

        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        logger.info("Exiting healthCheck() Controller");
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


    /**
     The idea for this api is to reload only the configs and not to recreate the beans,
     if configs are updated such that beans need to be created then it's better to restart
     the service.
     */
    @GetMapping("load_config_values")
    ResponseEntity<ObjectNode> loadConfigValues(HttpServletRequest request) {
        logger.info("Entering loadConfigValues() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            Init.loadConfigPropertiesFromFile();
            coreService.loadConfigValueFromDB("*");
            response = Util.getSuccessResponse("Successfully loaded the config values");

        } catch (Exception exception) {
            return Util.getErrorResponse(new ServiceException());
        }
        logger.info("Exiting loadConfigValues() Controller");
        return response;
    }

    @GetMapping("render_html")
    public String renderHtml(Model model) {
        logger.info("Entering renderHtml() Controller");
        model.addAttribute("variable_data", "PG");
        logger.info("Exiting renderHtml() Controller");
        return "SampleHtml";
    }
}
