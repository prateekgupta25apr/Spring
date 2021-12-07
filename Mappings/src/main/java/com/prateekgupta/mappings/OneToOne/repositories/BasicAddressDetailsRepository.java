package com.prateekgupta.mappings.OneToOne.repositories;

import com.prateekgupta.mappings.OneToOne.entity.BasicAddressDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

public interface BasicAddressDetailsRepository extends
        JpaRepositoryImplementation<BasicAddressDetails,Integer> {
    @Query("from BasicAddressDetails b inner join fetch b.address " +
            "where b.basicDetailsId=:id")
    BasicAddressDetails getBasicAddressDetailsById(@Param("id") int id);
}
