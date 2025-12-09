package prateek_gupta.SampleProject.prateek_gupta;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.boot.system.ApplicationHome;
import prateek_gupta.SampleProject.utils.Util;

import java.lang.reflect.Method;
import java.util.*;

import static prateek_gupta.SampleProject.prateek_gupta.ProjectSettings.*;

public class Init {

    public static Map<String,Object> configuration_properties=new HashMap<>();

    public static String projectDir ="";


    static {
        try {
            ApplicationHome home = new ApplicationHome();
            projectDir =home.getDir().getAbsolutePath()+"\\SampleProject\\src\\main\\";
            preConstructMethodExecution();
        } catch (Exception e) {
            ServiceException.logException(e);
        }
    }

    @PreConstructMethod
    public static void loadConfigPropertiesFromFile() throws Exception {
        configuration_properties=Utils.loadPropertiesFromFile(
                configuration_properties_file_path,
                ProjectSettings.required_fields,
                ProjectSettings.expected_fields,false);
    }

    public static Object getConfiguration(String key,String value){
        return configuration_properties.getOrDefault(key,value);
    }

    public static ConfigurationBuilder getScanConfiguration(){
        ConfigurationBuilder configurationBuilder;
        if(!scanned_files.isEmpty()){
            configurationBuilder=new ConfigurationBuilder()
                    .addScanners(Scanners.MethodsAnnotated);

            for(Class<?> classToBeScanned:scanned_files)
                configurationBuilder.addUrls(ClasspathHelper.forClass(classToBeScanned));

        }
        else
            configurationBuilder=new ConfigurationBuilder()
                    .forPackage("prateek_gupta.SampleProject")
                    .addScanners(Scanners.MethodsAnnotated);

        return configurationBuilder;
    }

    public static void preConstructMethodExecution()
            throws Exception {
        Reflections reflections = new Reflections(getScanConfiguration());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PreConstructMethod.class);
        System.out.println("PreConstructMethods : "+methods);

        for (Method method:methods) {
            if(method.getParameters().length>0)
                method.invoke(Util.getClassObject(method),
                    method.getAnnotation(PreConstructMethod.class).value());
            else
                method.invoke(Util.getClassObject(method));
        }

    }

    public static void postConstructMethodExecution()
            throws Exception {
        Reflections reflections = new Reflections(getScanConfiguration());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PostConstructMethod.class);
        System.out.println("PostConstructMethods : "+methods);

        for (Method method:methods)
            method.invoke(Util.getClassObject(method),
                    method.getAnnotation(PostConstructMethod.class).value());

    }
}
