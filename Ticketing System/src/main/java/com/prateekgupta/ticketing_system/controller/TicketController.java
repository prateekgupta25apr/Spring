package com.prateekgupta.ticketing_system.controller;

import com.prateekgupta.ticketing_system.dto.TicketsDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    @PostMapping("add-ticket")
    Object addTicket(TicketsDTO dto){
        return "Add ticket api";
    }

    @GetMapping("get-tickets")
    Object getTickets(){
        return "Get tickets api";
    }

    @PostMapping("update-ticket")
    Object updateTicket(TicketsDTO dto){
        return "Update ticket api";
    }
}
