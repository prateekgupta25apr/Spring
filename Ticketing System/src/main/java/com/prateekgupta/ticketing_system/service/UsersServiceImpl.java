package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.dto.UsersDTO;
import com.prateekgupta.ticketing_system.entity.UsersEntity;
import com.prateekgupta.ticketing_system.repository.UsersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    UsersRepo repo;

    @Autowired
    MappingService mappingService;

    @Override
    public Object addUser(UsersDTO dto) {
        if (dto.getFirstName() == null) {
            logger.warn("First name not found");
            return "Please provide first name";
        }

        if (dto.getLastName() == null) {
            logger.warn("Last name not found");
            return "Please provide lastname";
        }

        if (dto.getEmail() == null) {
            logger.warn("Email not found");
            return "Please enter email";
        }

        if (dto.getContactNumber() < 6000000000L) {
            logger.warn("Contact Number not found");
            return "Please enter contact number";
        }

        if (dto.getCity() == null) {
            logger.warn("City not found");
            return "Please enter your city name";
        }

        if (dto.getCountry() == null) {
            logger.warn("Country not found");
            return "Please enter your country name";
        }

        UsersEntity entity = new UsersEntity();
        try {
            BeanUtils.copyProperties(dto, entity);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "An internal issue occurred.Please try again";
        }

        repo.save(entity);
        return "User details saved";
    }

    @Override
    public Object getUsers() {
        List<UsersDTO> dtos = new ArrayList<>();
        for (UsersEntity entity : repo.findAll()) {
            UsersDTO dto = new UsersDTO();
            try {
                BeanUtils.copyProperties(entity, dto);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());
                return "An internal issue occurred.Please try again";
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Object updateUser(UsersDTO dto) {
        UsersEntity entity = repo.getById(dto.getId());
        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }


        if (dto.getMiddleName() != null) {
            entity.setMiddleName(dto.getMiddleName());
        }

        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getContactNumber() != 0) {
            if (dto.getContactNumber() < 6000000000L) {
                logger.warn("Contact Number not found");
                return "Please enter contact number";
            } else entity.setContactNumber(dto.getContactNumber());
        }

        if (dto.getCity() != null) {
            entity.setCity(dto.getCity());
        }

        if (dto.getState()!=null){
            entity.setState(dto.getState());
        }

        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }

        repo.save(entity);
        mappingService.updateUserDetails(entity);
        return "User details updated";
    }
}
