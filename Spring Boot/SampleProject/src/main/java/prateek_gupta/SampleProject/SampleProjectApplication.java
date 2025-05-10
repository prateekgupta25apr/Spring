package prateek_gupta.SampleProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import static prateek_gupta.SampleProject.prateek_gupta.ProjectSettings.
		configuration_properties_file_path;

@SpringBootApplication
@PropertySource("file:"+configuration_properties_file_path)
public class SampleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleProjectApplication.class, args);
		System.out.println("Good to go");
	}

}
