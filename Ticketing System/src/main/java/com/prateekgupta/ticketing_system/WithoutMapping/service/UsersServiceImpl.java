package com.prateekgupta.ticketing_system.WithoutMapping.service;

import com.prateekgupta.ticketing_system.WithoutMapping.dto.UsersDTO;
import com.prateekgupta.ticketing_system.WithoutMapping.entity.UsersEntity;
import com.prateekgupta.ticketing_system.WithoutMapping.repository.UsersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public Object addAll(List<UsersDTO> dtos) {
        for (int i = 0; i < dtos.size(); i++) {
            UsersDTO dto = dtos.get(i);
            if (dto.getFirstName() == null) {
                logger.warn("First name not found for object " + (i + 1));
                return "Please provide first name for object " + (i + 1);
            }

            if (dto.getLastName() == null) {
                logger.warn("Last name not found" + " for object " + (i + 1));
                return "Please provide lastname" + " for object " + (i + 1);
            }

            if (dto.getEmail() == null) {
                logger.warn("Email not found" + " for object " + (i + 1));
                return "Please enter email" + " for object " + (i + 1);
            }

            if (dto.getContactNumber() < 6000000000L) {
                logger.warn("Contact Number not found" + " for object " + (i + 1));
                return "Please enter contact number" + " for object " + (i + 1);
            }

            if (dto.getCity() == null) {
                logger.warn("City not found" + " for object " + (i + 1));
                return "Please enter your city name" + " for object " + (i + 1);
            }

            if (dto.getCountry() == null) {
                logger.warn("Country not found" + " for object " + (i + 1));
                return "Please enter your country name" + " for object " + (i + 1);
            }
        }
        List<UsersEntity> entities = new ArrayList<>();
        try {
            for (UsersDTO dto : dtos) {
                UsersEntity entity = new UsersEntity();
                BeanUtils.copyProperties(dto, entity);
                entities.add(entity);
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "An internal issue occurred.Please try again";
        }

        repo.saveAll(entities);
        return "All user details saved successfully";
    }

    @Override
    public Object getUsers() {
        List<UsersDTO> dtos = new ArrayList<>();
        for (UsersEntity entity : repo.findAll()) {
            UsersDTO dto = new UsersDTO();
            try {
                logger.info(entity.toString());
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
    public Object getUserById(UsersDTO dto) {
        try {
            UsersEntity entity = repo.findById(dto.getId());
            if (entity == null) throw new EntityNotFoundException();
            return entity;
        } catch (EntityNotFoundException e) {
            logger.warn("Invalid User Id provided");
            return "Please enter valid user id";
        }
    }

    @Override
    public Object updateUser(UsersDTO dto) {
        UsersEntity entity;
        if (dto.getId() <= 0) {
            logger.warn("User Id not found");
            return "Please enter valid user id";
        } else {
            try {
                entity = repo.findById(dto.getId());
                if (entity == null) throw new EntityNotFoundException();
            } catch (EntityNotFoundException e) {
                logger.warn("Invalid User Id provided");
                return "Please enter valid user id";
            }
        }

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

        if (dto.getState() != null) {
            entity.setState(dto.getState());
        }

        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }

        repo.save(entity);
        mappingService.updateUserDetails(entity);
        return "User details updated";
    }

    @Override
    public Object deleteUser(UsersDTO dto) {
        try {
            UsersEntity entity = repo.findById(dto.getId());
            if (entity == null) throw new EntityNotFoundException();
            repo.delete(entity);
            return "User details deleted successfully";
        } catch (EntityNotFoundException e) {
            logger.warn("Invalid User Id provided");
            return "Please enter valid user id";
        }
    }

    @Override
    public Object updateAll(List<UsersDTO> dtos) {
        for (int i = 0; i < dtos.size(); i++) {
            UsersDTO dto = dtos.get(i);
            UsersEntity entity;
            logger.warn("before: "+repo.getById(4));
            logger.warn("before: "+repo.getById(2));
            if (dto.getId() <= 0) {
                logger.warn("User Id not found" + " for object " + (i + 1));
                return "Please enter valid user id" + " for object " + (i + 1);
            } else {
                try {
                    entity = repo.findById(dto.getId());
                    if (entity == null) throw new EntityNotFoundException();
                } catch (EntityNotFoundException e) {
                    logger.warn("Invalid User Id provided" + " for object " + (i + 1));
                    return "Please enter valid user id" + " for object " + (i + 1);
                }
            }

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
                    logger.warn("Contact Number not found" + " for object " + (i + 1));
                    return "Please enter contact number" + " for object " + (i + 1);
                } else entity.setContactNumber(dto.getContactNumber());
            }

            if (dto.getCity() != null) {
                entity.setCity(dto.getCity());
            }

            if (dto.getState() != null) {
                entity.setState(dto.getState());
            }

            if (dto.getCountry() != null) {
                entity.setCountry(dto.getCountry());
            }

            repo.save(entity);
            mappingService.updateUserDetails(entity);
        }
        return "Updated details of all users";
    }

//    public static void main(String[] args) {
//        UsersEntity entity=new UsersEntity();
//        repo.save(entity);
//    }
}
