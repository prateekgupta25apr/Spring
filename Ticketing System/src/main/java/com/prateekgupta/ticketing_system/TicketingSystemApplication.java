package com.prateekgupta.ticketing_system;

import com.prateekgupta.ticketing_system.WithMapping.entity.TicketsEntityMapping;
import com.prateekgupta.ticketing_system.WithMapping.entity.UsersEntityMapping;
import com.prateekgupta.ticketing_system.WithMapping.repository.TicketsMappingRepository;
import com.prateekgupta.ticketing_system.WithMapping.repository.UsersMappingRepository;
import com.prateekgupta.ticketing_system.WithoutMapping.entity.TicketsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class TicketingSystemApplication implements CommandLineRunner {

	@Autowired
	UsersMappingRepository userRepo;

	@Autowired
	TicketsMappingRepository ticketRepo;

	public static void main(String[] args) {
		SpringApplication.run(TicketingSystemApplication.class, args);
		Logger logger= LoggerFactory.getLogger(TicketingSystemApplication.class);
		logger.info("Good to go");
	}

	@Override
	public void run(String... args) throws Exception {
		UsersEntityMapping user=new UsersEntityMapping();
		TicketsEntityMapping ticket=new TicketsEntityMapping();

		user.setId(1);
		user.setFirstName("Prateek");
		user.setMiddleName("");
		user.setLastName("Gupta");
		user.setEmail("p@gmail.com");
		user.setContactNumber(987654321L);
		user.setAddress("");
		user.setCity("Bangalore");
		user.setState("");
		user.setCountry("India");

		ticket.setId(3);
		ticket.setProductName("Wolken");
		ticket.setProductModelNumber(12345);
		ticket.setProductId("A-1");
		ticket.setTicketTitle("First ticket");
		ticket.setTicketDescription("issue");
		ticket.setDate(LocalDate.now());
		ticket.setOrderId("12345");
		ticket.setPriority(TicketsEntity.Priority.HIGH);
		ticket.setPaymentMode("CASH");
		ticket.setUser(userRepo.getById(1));

		ticketRepo.save(ticket);

		System.out.println(ticketRepo.getTicketById(3));
	}
}
