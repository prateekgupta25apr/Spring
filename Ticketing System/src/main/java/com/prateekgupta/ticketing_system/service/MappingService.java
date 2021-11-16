package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.entity.TicketsEntity;
import com.prateekgupta.ticketing_system.entity.UsersEntity;

public interface MappingService {
    void addMapping(UsersEntity userEntity, TicketsEntity ticketEntity);
}
