package com.prateekgupta.ticketing_system.WithMapping.controller;

import com.prateekgupta.ticketing_system.WithMapping.dto.UsersMappingDTO;
import com.prateekgupta.ticketing_system.WithMapping.service.UsersMappingService;
import com.prateekgupta.ticketing_system.WithoutMapping.controller.UsersController;
import com.prateekgupta.ticketing_system.WithoutMapping.dto.UsersDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mapping")
public class UsersMappingController {

    @Autowired
    UsersMappingService service;

    private final Logger logger = LoggerFactory.getLogger(UsersMappingController.class);

    @PostMapping("add-user")
    Object addUser(@RequestBody UsersMappingDTO dto){
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.addUser(dto);
    }

    @PostMapping("add-all-users")
    Object addAll(@RequestBody List<UsersMappingDTO> dtos) {
        if (dtos == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.addAll(dtos);
    }

    @PostMapping("get-by-id")
    Object getById(@RequestBody UsersMappingDTO dto) {
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.getUserById(dto);
    }

    @GetMapping("get-users")
    Object getUsers() {
        logger.info("Proceeding to service layer");
        return service.getUsers();
    }

    @PostMapping("update-user")
    Object updateUser(@RequestBody UsersMappingDTO dto) {
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.updateUser(dto);
    }

    @PostMapping("update-all-user")
    Object updateAllUser(@RequestBody List<UsersMappingDTO> dtos){
        if (dtos == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.updateAllUser(dtos);
    }

    @DeleteMapping("delete-user")
    Object deleteUser(@RequestBody UsersMappingDTO dto) {
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.deleteUser(dto);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    Object exceptionHandler() {
        logger.error("JSON data provided by the user in wrong format");
        return "Please provide \n" +
                "1. firstName in text format\n" +
                "2. middleName in numeric format\n" +
                "3. lastName in text format\n" +
                "4. email in text format\n" +
                "5. contactNumber in numeric format\n" +
                "6. address in the text format\n" +
                "7. city in text format\n" +
                "8. state in text format\n" +
                "9. country in text format\n " +
                "while adding users details and " +
                "in case of get-by-id/updating/deleting user details " +
                "user \"id\" must be provided.";
    }
}
