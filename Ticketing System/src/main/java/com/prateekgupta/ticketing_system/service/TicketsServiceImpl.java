package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.dto.TicketsDTO;
import com.prateekgupta.ticketing_system.entity.TicketsEntity;
import com.prateekgupta.ticketing_system.repository.TicketsRepo;
import com.prateekgupta.ticketing_system.repository.UsersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketsServiceImpl implements TicketsService {

    @Autowired
    TicketsRepo repo;

    @Autowired
    MappingService mappingService;

    @Autowired
    UsersRepo usersRepo;

    private final Logger logger = LoggerFactory.getLogger(TicketsServiceImpl.class);

    @Override
    public Object addTicket(TicketsDTO dto) {
        TicketsEntity entity = new TicketsEntity();
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

        if (dto.getUserId()<=0){
            logger.warn("User Id not found");
            return "Please enter your User Id";
        }
        try {
            BeanUtils.copyProperties(dto, entity);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "An internal issue occurred.Please try again";
        }
        repo.save(entity);
        mappingService.addMapping(usersRepo.getById(entity.getUserId()),entity);
        return "Ticket added successfully";
    }

    @Override
    public Object getTickets() {
        List<TicketsDTO> dtos = new ArrayList<>();
        for (TicketsEntity entity : repo.findAll()) {
            TicketsDTO dto = new TicketsDTO();
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
    public Object updateTickets(TicketsDTO dto) {
        TicketsEntity entity = repo.getById(dto.getId());

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

        if (dto.getUserId()>0){
            entity.setUserId(dto.getUserId());
        }

        repo.save(entity);
        mappingService.updateTicketDetails(entity);
        return "Ticket updated successfully";
    }
}
