package prateek_gupta.SampleProject.prateek_gupta;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Init {
    public static Map<String,Object> configuration_properties=new HashMap<>();

    public static void onLoad() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(
                ProjectSettings.configuration_properties_file_path)) {
            properties.load(fis);
        }

        for (String key : properties.stringPropertyNames()) {
            configuration_properties.put(key, properties.getProperty(key));
        }
    }
}
