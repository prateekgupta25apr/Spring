package com.prateekgupta.mappings.OneToOne.repositories;

import com.prateekgupta.mappings.OneToOne.entity.BasicDetails;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface BasicDetailsRepo extends
        JpaRepositoryImplementation<BasicDetails,Integer> {
}
