package prateek_gupta.SampleProject;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

import java.util.logging.LogManager;

import static prateek_gupta.SampleProject.prateek_gupta.ProjectSettings.
		configuration_properties_file_path;

@SpringBootApplication
@PropertySource("file:"+configuration_properties_file_path)
public class SampleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleProjectApplication.class, args);
		System.out.println("Good to go");
	}

	@SuppressWarnings("unused")
    @PostConstruct
	public void onLoad() {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

}
