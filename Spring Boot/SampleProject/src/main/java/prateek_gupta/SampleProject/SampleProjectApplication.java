package prateek_gupta.SampleProject;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import prateek_gupta.SampleProject.core.CoreService;
import prateek_gupta.SampleProject.prateek_gupta.Init;
import prateek_gupta.SampleProject.prateek_gupta.ServiceException;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.logging.LogManager;

import static prateek_gupta.SampleProject.prateek_gupta.ProjectSettings.
		configuration_properties_file_path;

@SpringBootApplication
@PropertySource("file:"+configuration_properties_file_path)
public class SampleProjectApplication {

	@Autowired
	CoreService coreService;

	public static void main(String[] args) {
		SpringApplication.run(SampleProjectApplication.class, args);
		System.out.println("Good to go");
	}

	@SuppressWarnings("unused")
    @PostConstruct
	public void onLoad() throws IOException, ServiceException {
		// Redirecting java.util.logging to SLF4J
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		// Loading Configuration Properties from file
		Init.onLoad();

		// Loading Configuration properties from DB
		coreService.loadConfigValueFromDB();
	}

}
