package com.prateekgupta.ticketing_system.WithMapping.service;

import com.prateekgupta.ticketing_system.WithMapping.dto.TicketsMappingDTO;

public interface TicketsMappingService {
    Object addTicket(TicketsMappingDTO dto);

    Object getTickets();

    Object updateTickets(TicketsMappingDTO dto);

    Object deleteTickets(TicketsMappingDTO dto);
}
