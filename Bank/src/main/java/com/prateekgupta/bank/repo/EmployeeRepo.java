package com.prateekgupta.bank.repo;

import com.prateekgupta.bank.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface EmployeeRepo extends JpaRepositoryImplementation<EmployeeEntity,Integer> {
    EmployeeEntity findByDesignation(String designation);
    EmployeeEntity findBySalary(float salary);
}
