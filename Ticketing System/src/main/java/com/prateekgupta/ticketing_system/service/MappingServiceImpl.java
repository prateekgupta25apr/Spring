package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.entity.MappingEntity;
import com.prateekgupta.ticketing_system.entity.TicketsEntity;
import com.prateekgupta.ticketing_system.entity.UsersEntity;
import com.prateekgupta.ticketing_system.repository.MappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MappingServiceImpl implements MappingService {

    @Autowired
    MappingRepo repo;

    @Override
    public void addMapping(UsersEntity userEntity, TicketsEntity ticketEntity) {
        MappingEntity entity=new MappingEntity();
        entity.setFirstName(userEntity.getFirstName());
        entity.setMiddleName(userEntity.getMiddleName());
        entity.setLastName(userEntity.getLastName());
        entity.setEmail(userEntity.getEmail());
        entity.setContactNumber(userEntity.getContactNumber());
        entity.setTicketTitle(ticketEntity.getTicketTitle());
        entity.setTicketDescription(ticketEntity.getTicketDescription());
        entity.setPriority(ticketEntity.getPriority());
        entity.setUserId(ticketEntity.getUserId());

        repo.save(entity);
    }
}
