package com.prateekgupta.ticketing_system.repository;

import com.prateekgupta.ticketing_system.entity.UsersEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface UsersRepo extends JpaRepositoryImplementation<UsersEntity,Integer> {
}
