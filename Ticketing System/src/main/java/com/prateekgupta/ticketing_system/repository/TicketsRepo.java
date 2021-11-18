package com.prateekgupta.ticketing_system.repository;

import com.prateekgupta.ticketing_system.entity.TicketsEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface TicketsRepo extends JpaRepositoryImplementation<TicketsEntity,Integer> {
    TicketsEntity findById(int id);
}
