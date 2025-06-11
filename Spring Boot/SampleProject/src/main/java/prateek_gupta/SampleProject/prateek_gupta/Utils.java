package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Utils {
    private static final Logger logger = LogManager.getLogger(
            Utils.class);

    public static void loadPropertiesFromFile(
            String filePath, Map<String,Object> propertiesHolder,
            List<String> requiredFields, List<String> expectedFields) throws Exception {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }

        List<String> requiredFieldValidation=new ArrayList<>(requiredFields);
        for (String key : requiredFields) {
            if (StringUtils.isNotBlank(properties.getProperty(key))) {
                propertiesHolder.put(key, properties.getProperty(key));
                requiredFieldValidation.remove(key);
            }
        }

        if (!requiredFieldValidation.isEmpty()){
            logger.error("Required field{} : {} not found.",
                    requiredFieldValidation.size() > 1 ? "s" : "",
                    requiredFieldValidation.toString().replace("[", "").
                    replace("]", ""));
            System.exit(1);
        }

        for (String key : expectedFields)
            if (StringUtils.isNotBlank(properties.getProperty(key)))
                propertiesHolder.put(key, properties.getProperty(key));

    }
}
