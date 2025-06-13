package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.*;

public class Utils {
    private static final Logger logger = LogManager.getLogger(
            Utils.class);

    public static Map<String,Object>
    loadPropertiesFromFile(String filePath,List<String> requiredFields,
                           List<String> expectedFields,boolean fetchAll) throws Exception {
        Map<String,Object> propertiesHolder=new HashMap<>();
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }

        if (fetchAll){
            Iterator<?> iterable = properties.propertyNames().asIterator();
            Object key;
            while (iterable.hasNext()) {
                key = iterable.next();
                propertiesHolder.put((String) key,
                        properties.getProperty(String.valueOf(key)));
            }
        }
        else {
            List<String> requiredFieldValidation = new ArrayList<>(requiredFields);
            for (String key : requiredFields) {
                if (StringUtils.isNotBlank(properties.getProperty(key))) {
                    propertiesHolder.put(key, properties.getProperty(key));
                    requiredFieldValidation.remove(key);
                }
            }

            if (!requiredFieldValidation.isEmpty()) {
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

        return propertiesHolder;
    }
}
