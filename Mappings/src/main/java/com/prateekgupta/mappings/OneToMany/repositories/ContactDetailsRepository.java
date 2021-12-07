package com.prateekgupta.mappings.OneToMany.repositories;

import com.prateekgupta.mappings.OneToMany.entity.ContactDetails;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface ContactDetailsRepository extends JpaRepositoryImplementation<ContactDetails, Integer> {
}