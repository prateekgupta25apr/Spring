package com.prateekgupta.mappings;

import com.prateekgupta.mappings.OneToMany.repositories.ContactDetailsRepository;
import com.prateekgupta.mappings.OneToMany.repositories.BasicContactDetailsRepository;
import com.prateekgupta.mappings.OneToOne.entity.AddressDetails;
import com.prateekgupta.mappings.OneToOne.entity.BasicAddressDetails;
import com.prateekgupta.mappings.OneToOne.repositories.AddressDetailsRepo;
import com.prateekgupta.mappings.OneToOne.repositories.BasicAddressDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MappingsApplication implements CommandLineRunner {
	@Autowired
	BasicAddressDetailsRepository basicAddressDetailsRepo;

	@Autowired
	AddressDetailsRepo addressDetailsRepo;

	@Autowired
	BasicContactDetailsRepository basicContactDetailsRepo;

	@Autowired
	ContactDetailsRepository contactDetailsRepository;

	static Logger logger= LoggerFactory.getLogger(MappingsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MappingsApplication.class, args);
		logger.info("All good");
	}

	@Override
	public void run(String... args) throws Exception {
//	    OneToOne add
		AddressDetails addressDetails = new AddressDetails();
		BasicAddressDetails basicAddressDetails = new BasicAddressDetails();
		basicAddressDetails.setBasicDetailsId(3);
		basicAddressDetails.setName("p");
		basicAddressDetails.setEmail("p@gmail.com");
		basicAddressDetails.setAddress(addressDetails);

		addressDetails.setId(2);
		addressDetails.setFlatNo("319");
		addressDetails.setStreetName("bel circle");
		addressDetails.setCity("pune");
		addressDetails.setState("karnataka");
		addressDetails.setCountry("India");
		addressDetails.setBasicAddressDetails(basicAddressDetails);
		addressDetailsRepo.save(addressDetails);
		basicAddressDetailsRepo.save(basicAddressDetails);

		// OneToOne-update
		BasicAddressDetails basicAddressDetails1=
				basicAddressDetailsRepo.getBasicAddressDetailsById(3);
		basicAddressDetails1.setName("PG");
		basicAddressDetailsRepo.save(basicAddressDetails1);

////	OneToMany-add
//		ContactDetails contactDetails1=new ContactDetails();
//		contactDetails1.setId(1);
//		contactDetails1.setContactNumber(9876543210L);
//		ContactDetails contactDetails2=new ContactDetails();
//		contactDetails2.setId(2);
//		contactDetails2.setContactNumber(9876543219L);
//
//		List<ContactDetails> contactDetailsList=new ArrayList<>();
//		contactDetailsList.add(contactDetails1);
//		contactDetailsList.add(contactDetails2);
//
//		contactDetailsRepository.save(contactDetails1);
//
//		BasicContactDetails basicContactDetails =new BasicContactDetails();
//		basicContactDetails.setBasicDetailsId(1);
//		basicContactDetails.setName("Prateek");
//		basicContactDetails.setEmail("p@gmail.com");
//		basicContactDetails.setContactDetails(contactDetailsList);
//
//		contactDetails1.setBasicContactDetails(basicContactDetails);
//		contactDetails2.setBasicContactDetails(basicContactDetails);
//		contactDetailsRepository.save(contactDetails1);
//		contactDetailsRepository.save(contactDetails2);
//
//		basicContactDetailsRepo.save(basicContactDetails);

//// 	OneToMany-update
//		BasicContactDetails b=basicContactDetailsRepo.getBasicDetailsById(2);
//		b.setName("G");
//		basicContactDetailsRepo.save(b);
	}
}
