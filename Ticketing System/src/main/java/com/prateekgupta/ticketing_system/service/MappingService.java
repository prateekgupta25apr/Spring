package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.dto.MappingDTO;
import com.prateekgupta.ticketing_system.entity.TicketsEntity;
import com.prateekgupta.ticketing_system.entity.UsersEntity;

import java.util.List;

public interface MappingService {
    void addMapping(UsersEntity userEntity, TicketsEntity ticketEntity);
    void updateUserDetails(UsersEntity entity);
    void updateTicketDetails(TicketsEntity entity);
    List<MappingDTO> getTicketsByUser(MappingDTO dto);
}
