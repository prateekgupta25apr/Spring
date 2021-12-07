package com.prateekgupta.ticketing_system.WithMapping.controller;

import com.prateekgupta.ticketing_system.WithMapping.service.TicketsMappingService;
import com.prateekgupta.ticketing_system.WithoutMapping.controller.TicketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mapping")
public class TicketsMappingController {
    private final Logger logger = LoggerFactory.getLogger(TicketsMappingController.class);

    @Autowired
    TicketsMappingService service;


}
