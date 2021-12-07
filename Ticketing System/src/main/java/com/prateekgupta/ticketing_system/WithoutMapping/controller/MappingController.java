package com.prateekgupta.ticketing_system.WithoutMapping.controller;

import com.prateekgupta.ticketing_system.WithoutMapping.dto.MappingDTO;
import com.prateekgupta.ticketing_system.WithoutMapping.service.MappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {

    private final Logger logger= LoggerFactory.getLogger(MappingController.class);

    @Autowired
    MappingService service;

    @PostMapping("get-tickets-by-user")
    Object getTicketsByUser(@RequestBody MappingDTO dto){
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.getTicketsByUser(dto);
    }
}
