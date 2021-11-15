package com.prateekgupta.ticketing_system.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prateekgupta.ticketing_system.dto.TicketsDTO;
import com.prateekgupta.ticketing_system.dto.UsersDTO;
import com.prateekgupta.ticketing_system.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
public class UserController {

    @Autowired
    UsersService service;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("add-user")
    Object addUsers(@RequestBody UsersDTO dto) {
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.addUser(dto);
    }

    @GetMapping("get-users")
    Object getUsers() {
        logger.info("Proceeding to service layer");
        return service.getUsers();
    }

    @PostMapping("update-user")
    Object updateUser(@RequestBody UsersDTO dto) {
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.updateUser(dto);
    }
}
