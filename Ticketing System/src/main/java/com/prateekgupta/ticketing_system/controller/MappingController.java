package com.prateekgupta.ticketing_system.controller;

import com.prateekgupta.ticketing_system.dto.MappingDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {

    @PostMapping("get-tickets-by-user")
    Object getTicketsByUser(MappingDTO dto){
        return "Get ticket by user api";
    }
}
