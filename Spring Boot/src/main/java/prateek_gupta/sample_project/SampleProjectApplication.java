package prateek_gupta.sample_project;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.PropertySource;

@org.springframework.boot.autoconfigure.SpringBootApplication
@PropertySource("file:configuration.txt")
public class SampleProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SampleProjectApplication.class, args);
		System.out.println("Good to go");
	}

}
