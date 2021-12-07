package com.prateekgupta.bank.service;

import com.prateekgupta.bank.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    Object save(EmployeeDTO dto);
    Object saveAll(List<EmployeeDTO> dtos);
    Object findAll();
    Object findByDesignation(String designation);
    Object findBySalary(float salary);
}
