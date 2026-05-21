package prateek_gupta.SampleProject.users.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prateek_gupta.SampleProject.core.SessionFilter;
import prateek_gupta.SampleProject.core.UserContext;
import prateek_gupta.SampleProject.project_utils.Init;
import prateek_gupta.SampleProject.users.service.UsersService;

@RestController
@RequestMapping("users")
public class UsersController {

    private final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    UsersService usersService;

    @PostMapping("login")
    ResponseEntity<ObjectNode> login(
            @RequestBody String data, HttpServletResponse httpServletResponse) {
        logger.info("Entering login() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            JSONObject result = usersService.login(JSONObject.fromObject(data),httpServletResponse);
            response = Init.getSuccessResponse(result);
        } catch (Exception exception) {
            return Init.getErrorResponse(exception);
        }
        logger.info("Exiting login() Controller");
        return response;
    }
}
