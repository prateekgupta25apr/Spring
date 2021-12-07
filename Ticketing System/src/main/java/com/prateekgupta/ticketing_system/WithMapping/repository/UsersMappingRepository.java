package com.prateekgupta.ticketing_system.WithMapping.repository;

import com.prateekgupta.ticketing_system.WithMapping.entity.UsersEntityMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

public interface UsersMappingRepository extends
        JpaRepositoryImplementation<UsersEntityMapping, Integer> {
    UsersEntityMapping findById(int id);
}