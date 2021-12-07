package com.prateekgupta.mappings.OneToMany.repositories;

import com.prateekgupta.mappings.OneToMany.entity.BasicContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BasicContactDetailsRepository extends
        JpaRepository<BasicContactDetails, Integer> {
    @Query("from BasicContactDetails b inner join fetch b.contactDetails " +
            "where b.basicDetailsId=:id")
    BasicContactDetails getBasicDetailsById(@Param("id") int id);
}