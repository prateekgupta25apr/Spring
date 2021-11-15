package com.prateekgupta.ticketing_system.controller;

import com.prateekgupta.ticketing_system.dto.TicketsDTO;
import com.prateekgupta.ticketing_system.service.TicketsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    private final Logger logger= LoggerFactory.getLogger(TicketController.class);

    @Autowired
    TicketsService service;

    @PostMapping("add-ticket")
    Object addTicket(@RequestBody TicketsDTO dto){
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.addTicket(dto);
    }

    @GetMapping("get-tickets")
    Object getTickets(){
        logger.info("Proceeding to service layer");
        return service.getTickets();
    }

    @PostMapping("update-ticket")
    Object updateTicket(@RequestBody TicketsDTO dto){
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        return service.updateTickets(dto);
    }
}
