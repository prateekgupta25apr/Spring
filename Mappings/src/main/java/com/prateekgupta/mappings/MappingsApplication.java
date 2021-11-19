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

		AddressDetails addressDetails = new AddressDetails();
		addressDetails.setId(2);
		addressDetails.setFlatNo("319");
		addressDetails.setStreetName("bel circle");
		addressDetails.setCity("pune");
		addressDetails.setState("karnataka");
		addressDetails.setCountry("India");
		addressDetailsRepo.save(addressDetails);

		BasicDetails basicDetails = new BasicDetails();
		basicDetails.setBasicDetailsId(3);
		basicDetails.setName("p");
		basicDetails.setEmail("p@gmail.com");
		basicDetails.setAddress(addressDetails);
		basicDetailsRepo.save(basicDetails);

	}
}
