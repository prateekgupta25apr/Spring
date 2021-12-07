package com.prateekgupta.ticketing_system.WithoutMapping.service;

import com.prateekgupta.ticketing_system.WithoutMapping.dto.TicketsDTO;

public interface TicketsService {
    Object addTicket(TicketsDTO dto);
    Object getTickets();
    Object updateTickets(TicketsDTO dto);
    Object deleteTickets(TicketsDTO dto);
}
