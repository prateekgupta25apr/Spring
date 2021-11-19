package com.prateekgupta.mappings;

import com.prateekgupta.mappings.OneToOne.entity.AddressDetails;
import com.prateekgupta.mappings.OneToOne.entity.BasicDetails;
import com.prateekgupta.mappings.OneToOne.repositories.AddressDetailsRepo;
import com.prateekgupta.mappings.OneToOne.repositories.BasicDetailsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MappingsApplication implements CommandLineRunner {
	@Autowired
	BasicDetailsRepo basicDetailsRepo;

	@Autowired
	AddressDetailsRepo addressDetailsRepo;

	static Logger logger= LoggerFactory.getLogger(MappingsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MappingsApplication.class, args);
		logger.info("Good to go");
	}

	@Override
	public void run(String... args) throws Exception {
		BasicDetails basicDetails = new BasicDetails();
		AddressDetails addressDetails = new AddressDetails();
		addressDetails.setId(1);
		addressDetails.setFlatNo("107");
		addressDetails.setStreetName("laxmipura");
		addressDetails.setCity("bangalore");
		addressDetails.setState("karnataka");
		addressDetails.setCountry("India");

		basicDetails.setBasicDetailsId(1);
		basicDetails.setName("Prateek");
		basicDetails.setEmail("p@gmail.com");
		basicDetails.setAddress(addressDetails);
		basicDetailsRepo.save(basicDetails);
		addressDetailsRepo.save(addressDetails);
		System.out.println("All good");

	}
}
