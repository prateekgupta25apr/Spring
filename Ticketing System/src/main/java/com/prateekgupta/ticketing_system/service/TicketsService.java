package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.dto.TicketsDTO;

public interface TicketsService {
    Object addTicket(TicketsDTO dto);
    Object getTickets();
    Object updateTickets(TicketsDTO dto);
    Object deleteTickets(TicketsDTO dto);
}
