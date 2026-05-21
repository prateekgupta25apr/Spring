package prateek_gupta.SampleProject.prateek_gupta;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;

public class Utils {
    private static final Logger logger = LogManager.getLogger(
            Utils.class);

    public static Map<String,Object> processCookie(
            boolean decode,String secretKey,String cookie) {
        //noinspection unchecked
        return (Map<String, Object>) processCookie(decode,secretKey,cookie,null);
    }

    public static Object processCookie(
            boolean decode,String secretKey,Map<String,Object> cookieData) {
        return processCookie(decode,secretKey,null,cookieData);
    }
    public static Object processCookie(
            boolean decode,String secretKey,String cookie,Map<String,Object> cookieData){

        if(decode){
            Map<String,Object> responseData;
            if (StringUtils.isNotBlank(cookie))
                responseData= Jwts.parser().verifyWith(
                                Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .build().parseSignedClaims(cookie).getPayload();
            else
                responseData=new  HashMap<>();
            return responseData;
        }
        else {
            String preparedCookie;
            if(cookieData!=null) {
                JwtBuilder builder = Jwts.builder();
                for (Map.Entry<String, Object> entry : cookieData.entrySet())
                    builder.claim(entry.getKey(), entry.getValue());
                preparedCookie= builder.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .compact();
            }
            else
                preparedCookie="";

            return preparedCookie;
        }

    }

    public static Map<String,Object>
    loadPropertiesFromFile(String filePath,List<String> requiredFields,
                           List<String> expectedFields,boolean fetchAll) throws Exception {
        Map<String,Object> propertiesHolder=new LinkedHashMap<>();
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

    public static String buildUrl(String relativeUrl){
        return URI.create(Init.getConfiguration("base_url","").toString())
                .resolve(Init.getConfiguration("context_path","").toString())
                .resolve(relativeUrl).toString();
    }

    public static Map<String,String> getContentType(String fileName){
        String contentType = HttpURLConnection.guessContentTypeFromName(fileName);
        String mainType;
        String subType;

        if (contentType != null && contentType.contains("/")) {
            String[] parts = contentType.split("/");
            mainType = parts[0];
            subType = parts[1];
        } else {
            mainType = "application";
            subType = "octet-stream";
            contentType = "application/octet-stream";
        }

        Map<String, String> result = new HashMap<>();
        result.put("main_type", mainType);
        result.put("sub_type", subType);
        result.put("content_type", contentType);

        return result;
    }
}
