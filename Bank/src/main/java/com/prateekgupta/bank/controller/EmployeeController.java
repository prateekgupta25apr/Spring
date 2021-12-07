package com.prateekgupta.bank.controller;

import com.prateekgupta.bank.dto.EmployeeDTO;
import com.prateekgupta.bank.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    EmployeeService service;

    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping("save")
    Object save(@RequestBody EmployeeDTO dto) {
        if (dto == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.save(dto);
    }

    @PostMapping("save-all")
    Object saveAll(@RequestBody List<EmployeeDTO> dtos) {
        if (dtos == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.saveAll(dtos);
    }

    @GetMapping("find-all")
    Object findAll() {
        return service.findAll();
    }

    @PostMapping("find-by-designation")
    Object findByDesignation(@RequestBody String designation) {
        if (designation == null) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.findByDesignation(designation);
    }

    @PostMapping("find-by-salary")
    Object findBySalary(@RequestBody float salary) {
        if (salary < 0) {
            logger.warn("Please enter necessary details");
            return "Please enter necessary details";
        }
        logger.info("Proceeding to Service layer");
        return service.findBySalary(salary);
    }
}
