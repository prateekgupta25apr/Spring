package com.prateekgupta.ticketing_system.WithoutMapping.repository;

import com.prateekgupta.ticketing_system.WithoutMapping.entity.MappingEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface MappingRepo extends JpaRepositoryImplementation<MappingEntity,Integer> {
    List<MappingEntity> findAllByUserId(int userId);
}
