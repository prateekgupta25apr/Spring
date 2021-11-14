package com.prateekgupta.ticketing_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketingSystemApplication.class, args);
		Logger logger= LoggerFactory.getLogger(TicketingSystemApplication.class);
		logger.info("Good to go");
	}

}
