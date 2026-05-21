package prateek_gupta.SampleProject;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import prateek_gupta.SampleProject.prateek_gupta.Init;

import jakarta.annotation.PostConstruct;

import java.util.logging.LogManager;

import static prateek_gupta.SampleProject.prateek_gupta.ProjectSettings.
		configuration_properties_file_path;

@SpringBootApplication
@PropertySource("file:"+configuration_properties_file_path)
@DependsOn("applicationContextSetter")
public class SampleProjectApplication {

	public static void main(String[] args) {
		// Redirecting java.util.logging to SLF4J, not using @PreConstructMethod because in
		// Java 21 and Spring Boot 3.2.5, following code needs to be executed before run method
		// and static block execution( execution of @PreConstructMethod annotated methods)
		// won't work
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		SpringApplication.run(SampleProjectApplication.class, args);
		System.out.println("Good to go");
	}


    @PostConstruct
	public void onLoad() throws Exception {
		Init.postConstructMethodExecution();
	}


}
