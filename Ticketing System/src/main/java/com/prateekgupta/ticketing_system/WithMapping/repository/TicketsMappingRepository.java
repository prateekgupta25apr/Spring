package com.prateekgupta.ticketing_system.WithMapping.repository;

import com.prateekgupta.ticketing_system.WithMapping.entity.TicketsEntityMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

public interface TicketsMappingRepository extends
        JpaRepositoryImplementation<TicketsEntityMapping, Integer> {

    @Query("from TicketsEntityMapping t inner join fetch t.user where t.id=:id")
    TicketsEntityMapping getTicketById(@Param("id") int id);
}