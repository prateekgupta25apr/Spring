package com.prateekgupta.ticketing_system.WithMapping.service;

import com.prateekgupta.ticketing_system.WithMapping.dto.TicketsMappingDTO;
import com.prateekgupta.ticketing_system.WithMapping.entity.TicketsEntityMapping;
import com.prateekgupta.ticketing_system.WithMapping.entity.UsersEntityMapping;
import com.prateekgupta.ticketing_system.WithMapping.repository.TicketsMappingRepository;
import com.prateekgupta.ticketing_system.WithMapping.repository.UsersMappingRepository;
import com.prateekgupta.ticketing_system.WithoutMapping.entity.TicketsEntity;
import com.prateekgupta.ticketing_system.WithoutMapping.service.TicketsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketsMappingServiceImpl implements TicketsMappingService {

    @Autowired
    TicketsMappingRepository repo;

    @Autowired
    UsersMappingRepository usersRepo;

    private final Logger logger = LoggerFactory.getLogger(TicketsMappingServiceImpl.class);


    @Override
    public Object addTicket(TicketsMappingDTO dto) {
        TicketsEntityMapping entity = new TicketsEntityMapping();
        if (dto.getProductName() == null) {
            logger.warn("Product Name not found");
            return "Please enter Product Name";
        }
        if (dto.getProductModelNumber() <= 0) {
            logger.warn("Product Model Number not found");
            return "Please enter Product Model Number";
        }

        if (dto.getTicketTitle() == null) {
            logger.warn("Ticket Title not found");
            return "Please enter some title for the Ticket";
        }

        if (dto.getTicketDescription() == null) {
            logger.warn("Ticket Description not found");
            return "Please enter Ticket description";
        }

        if (dto.getDate() == null) {
            logger.warn("Date not found");
            return "Please enter date";
        }

        if (dto.getOrderId() == null) {
            logger.warn("Order id not found");
            return "Please enter order id";
        }

        if (dto.getPriority() == null) {
            logger.warn("Priority not found");
            return "Please enter your priority among HIGH, MEDIUM, LOW";
        } else {
            switch (dto.getPriority().toUpperCase()) {
                case "HIGH":
                    entity.setPriority(TicketsEntity.Priority.HIGH);
                    break;
                case "MEDIUM":
                    entity.setPriority(TicketsEntity.Priority.MEDIUM);
                    break;
                case "LOW":
                    entity.setPriority(TicketsEntity.Priority.LOW);
                    break;
                default:
                    return "Please enter priority among HIGH, MEDIUM, LOW only";
            }
        }

        if (dto.getPaymentMode() == null) {
            logger.warn("Payment Mode not found");
            return "Please mention the mode of Payment";
        } else {
            dto.setPaymentMode(dto.getPaymentMode().toUpperCase());
            switch (dto.getPaymentMode()) {
                case "ONLINE":
                    entity.setPaymentMode(TicketsEntity.PaymentMode.ONLINE.toString());
                    break;
                case "CARD":
                    entity.setPaymentMode(TicketsEntity.PaymentMode.CARD.toString());
                    logger.info(TicketsEntity.PaymentMode.CARD.toString());
                    break;
                case "CASH":
                    entity.setPaymentMode(TicketsEntity.PaymentMode.CASH.toString());
                    break;
                default:
                    return "Please enter the mode of payment among" +
                            " ONLINE, CARD, CASH only";
            }
        }

        UsersEntityMapping usersEntity;
        if (dto.getUserId() <= 0) {
            logger.warn("User Id not found");
            return "Please enter your User Id";
        } else {
            try {
                usersEntity = usersRepo.findById(dto.getUserId());
                if (usersEntity == null) throw new EntityNotFoundException();
            } catch (EntityNotFoundException e) {
                logger.error("Invalid User Id");
                return "Please enter valid user id";
            }
        }

        try {
            BeanUtils.copyProperties(dto, entity, "user");
            entity.setUser(usersEntity);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "An internal issue occurred.Please try again";
        }

        repo.save(entity);

        return "Ticket added successfully";
    }

    @Override
    public Object getTickets() {
        List<TicketsMappingDTO> dtos = new ArrayList<>();
        for (TicketsEntityMapping entity : repo.getAllTickets()) {
            TicketsMappingDTO dto = new TicketsMappingDTO();
            try {
                BeanUtils.copyProperties(entity, dto, "priority,user");
                switch (entity.getPriority()) {
                    case HIGH:
                        dto.setPriority(TicketsEntity.Priority.HIGH.toString());
                        break;
                    case MEDIUM:
                        dto.setPriority(TicketsEntity.Priority.MEDIUM.toString());
                        break;
                    case LOW:
                        dto.setPriority(TicketsEntity.Priority.LOW.toString());
                        break;
                    default:
                        return "Please enter priority among HIGH, MEDIUM, LOW only";
                }
                dto.setUserId(entity.getUser().getId());
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());
                return "An internal issue occurred.Please try again";
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Object updateTickets(TicketsMappingDTO dto) {
        TicketsEntityMapping entity;
        if (dto.getId() <= 0) {
            logger.warn("Ticket Id not found");
            return "Please enter valid ticket id";
        } else {
            try {
                entity = repo.getTicketById(dto.getId());
                if (entity == null) throw new EntityNotFoundException();
            } catch (EntityNotFoundException e) {
                logger.error("Invalid Ticket id provided");
                return "Please enter valid ticket id";
            }
        }

        if (dto.getProductName() != null) {
            entity.setProductName(dto.getProductName());
        }

        if (dto.getProductModelNumber() > 0) {
            entity.setProductModelNumber(dto.getProductModelNumber());
        }

        if (dto.getProductId() != null) {
            entity.setProductId(dto.getProductId());
        }

        if (dto.getTicketTitle() != null) {
            entity.setTicketTitle(dto.getTicketTitle());
        }

        if (dto.getTicketDescription() != null) {
            entity.setTicketDescription(dto.getTicketDescription());
        }

        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        }

        if (dto.getOrderId() != null) {
            entity.setOrderId(dto.getOrderId());
        }

        if (dto.getPriority() != null) {
            switch (dto.getPriority().toUpperCase()) {
                case "HIGH":
                    entity.setPriority(TicketsEntity.Priority.HIGH);
                    break;
                case "MEDIUM":
                    entity.setPriority(TicketsEntity.Priority.MEDIUM);
                    break;
                case "LOW":
                    entity.setPriority(TicketsEntity.Priority.LOW);
                    break;
                default:
                    return "Please enter priority among HIGH, MEDIUM, LOW only";
            }
        }

        if (dto.getPaymentMode() != null) {
            switch (dto.getPaymentMode().toUpperCase()) {
                case "ONLINE":
                    entity.setPaymentMode(TicketsEntity.PaymentMode.ONLINE.toString());
                    break;
                case "CARD":
                    entity.setPaymentMode(TicketsEntity.PaymentMode.CARD.toString());
                    logger.info(TicketsEntity.PaymentMode.CARD.toString());
                    break;
                case "CASH":
                    entity.setPaymentMode(TicketsEntity.PaymentMode.CASH.toString());
                    break;
                default:
                    return "Please enter the mode of payment among" +
                            " ONLINE, CARD, CASH only";
            }
        }

        if (dto.getUserId() > 0) {
            UsersEntityMapping usersEntity=usersRepo.findById(dto.getUserId());
            if (usersEntity==null){
                logger.error("Invalid User id provided");
                return "Please enter valid user id";
            }
            entity.setUser(usersEntity);
        }
        repo.save(entity);

        return "Ticket updated successfully";
    }

    @Override
    public Object deleteTickets(TicketsMappingDTO dto) {
        try {
            TicketsEntityMapping entity = repo.getTicketById(dto.getId());
            System.out.println(entity);
            if (entity == null) throw new EntityNotFoundException();
            repo.delete(entity);
            return "Ticket deleted successfully";
        } catch (EntityNotFoundException e) {
            logger.error("Invalid Ticket id provided");
            return "Please enter valid ticket id";
        }
    }
}
