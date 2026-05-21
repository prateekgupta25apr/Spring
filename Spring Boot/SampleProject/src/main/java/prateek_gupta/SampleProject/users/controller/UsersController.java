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

    @PostMapping("sign_up")
    ResponseEntity<ObjectNode> signUp(
            @RequestBody String data, HttpServletResponse httpServletResponse) {
        logger.info("Entering signUp() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            JSONObject result = usersService.signUp(JSONObject.fromObject(data),httpServletResponse);
            response = Init.getSuccessResponse(result);
        } catch (Exception exception) {
            return Init.getErrorResponse(exception);
        }
        logger.info("Exiting signUp() Controller");
        return response;
    }

    @PostMapping("forgot_password")
    ResponseEntity<ObjectNode> forgotPassword(@RequestBody String data) {
        logger.info("Entering forgotPassword() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            JSONObject result = usersService.forgotPassword(JSONObject.fromObject(data));
            response = Init.getSuccessResponse(result);
        } catch (Exception exception) {
            return Init.getErrorResponse(exception);
        }
        logger.info("Exiting forgotPassword() Controller");
        return response;
    }

    @PostMapping("logout")
    ResponseEntity<ObjectNode> logout() {
        logger.info("Entering logout() Controller");
        ResponseEntity<ObjectNode> response;
        try {
            SessionFilter.updateSessionForLogout();
            JSONObject result = new JSONObject();
            result.put("message", "Successfully logged out");
            response = Init.getSuccessResponse(result);
        } catch (Exception exception) {
            return Init.getErrorResponse(exception);
        }
        logger.info("Exiting logout() Controller");
        return response;
    }
}
