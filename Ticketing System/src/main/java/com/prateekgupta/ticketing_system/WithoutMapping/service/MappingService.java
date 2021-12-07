package com.prateekgupta.ticketing_system.WithoutMapping.service;

import com.prateekgupta.ticketing_system.WithoutMapping.dto.MappingDTO;
import com.prateekgupta.ticketing_system.WithoutMapping.entity.TicketsEntity;
import com.prateekgupta.ticketing_system.WithoutMapping.entity.UsersEntity;

import java.util.List;

public interface MappingService {
    void addMapping(UsersEntity userEntity, TicketsEntity ticketEntity);
    void updateUserDetails(UsersEntity entity);
    void updateTicketDetails(TicketsEntity entity);
    List<MappingDTO> getTicketsByUser(MappingDTO dto);
}
