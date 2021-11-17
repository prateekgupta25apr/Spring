package com.prateekgupta.ticketing_system.controller;

import com.prateekgupta.ticketing_system.dto.TicketsDTO;
import com.prateekgupta.ticketing_system.service.TicketsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import java.time.format.DateTimeParseException;

@RestController
public class TicketController {

    private final Logger logger= LoggerFactory.getLogger(TicketController.class);

    @Autowired
    TicketsService service;


        @PostMapping("add-ticket")
        Object addTicket (@RequestBody TicketsDTO dto){
        if (dto == null) {
            logger.warn("DTO is null");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to service layer");
        logger.info(dto.toString());
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

    @ExceptionHandler({HttpMessageNotReadableException.class})
    Object exceptionHandler(){
            logger.error("JSON data provided by the user in wrong format");
            return "Please provide \n" +
                    "1. productName in text format\n" +
                    "2. productModelNumber in numeric format\n" +
                    "3. productId in text format\n" +
                    "4. ticketTitle in text format\n" +
                    "5. ticketDescription in text format\n" +
                    "6. date in the text format and in pattern dd/mm/yyyy only\n" +
                    "7. orderId in text format\n" +
                    "8. priority among HIGH, MEDIUM, LOW only\n" +
                    "9. paymentMode among ONLINE, CARD, CASH only\n" +
                    "10. userId in numeric format";
    }


}
