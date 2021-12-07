package com.prateekgupta.bank.service;

import com.prateekgupta.bank.dto.EmployeeDTO;
import com.prateekgupta.bank.entity.EmployeeEntity;
import com.prateekgupta.bank.repo.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepo repo;

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    public String save(EmployeeDTO dto) {
        if (dto.getName() == null) {
            logger.warn("Name not found");
            return "Please enter valid Name";
        }

        if (dto.getDesignation() == null) {
            logger.warn("Designation not found");
            return "Please enter valid designation";
        }

        if (dto.getContactNumber() == 0) {
            logger.warn("Contact Number not found");
            return "Please enter valid contact number";
        }

        if (dto.getSalary() < 0) {
            logger.warn("Salary not found");
            return "Please enter valid salary";
        }

        EmployeeEntity entity = new EmployeeEntity();
        try {
            BeanUtils.copyProperties(dto, entity);
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return "Invalid Employee Details provided";
        }

        try {
            repo.save(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Invalid Employee Details provided";
        }

        return "Employee details saved";
    }

    @Override
    public String saveAll(List<EmployeeDTO> dtos) {
        List<EmployeeEntity> entities = new ArrayList<>();
        for (EmployeeDTO dto : dtos) {
            if (dto.getName() == null) {
                logger.warn("Name not found");
                return "Please enter valid Name";
            }

            if (dto.getDesignation() == null) {
                logger.warn("Designation not found");
                return "Please enter valid designation";
            }

            if (dto.getContactNumber() == 0) {
                logger.warn("Contact Number not found");
                return "Please enter valid contact number";
            }

            if (dto.getSalary() < 0) {
                logger.warn("Salary not found");
                return "Please enter valid salary";
            }

            EmployeeEntity entity = new EmployeeEntity();
            try {
                BeanUtils.copyProperties(dto, entity);
            } catch (NullPointerException e) {
                logger.error(e.getMessage());
                return "Invalid Employee Details provided";
            }
            entities.add(entity);
        }
        try{
            repo.saveAll(entities);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Invalid Employee Details provided";
        }
        return "All Employee details saved";
    }

    @Override
    public List<EmployeeDTO> findAll() {
        List<EmployeeDTO> dtos = new ArrayList<>();
        for (EmployeeEntity entity : repo.findAll()) {
            EmployeeDTO dto = new EmployeeDTO();
            BeanUtils.copyProperties(entity, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Object findByDesignation(String designation) {
        EmployeeDTO dto = new EmployeeDTO();
        try{
            BeanUtils.copyProperties(repo.findByDesignation(designation), dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "No account found with provided designation";
        }
        return dto;
    }

    @Override
    public Object findBySalary(float salary) {
        EmployeeDTO dto = new EmployeeDTO();
        try{
            BeanUtils.copyProperties(repo.findBySalary(salary), dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "No account found with provided salary";
        }
        return dto;
    }
}
