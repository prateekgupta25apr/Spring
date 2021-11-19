package com.prateekgupta.mappings.OneToOne.repositories;

import com.prateekgupta.mappings.OneToOne.entity.AddressDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDetailsRepo extends JpaRepository<AddressDetails,Integer> {
}
