package com.prateekgupta.ticketing_system.WithoutMapping.repository;

import com.prateekgupta.ticketing_system.WithoutMapping.entity.UsersEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface UsersRepo extends JpaRepositoryImplementation<UsersEntity,Integer> {
    UsersEntity findById(int id);
}
