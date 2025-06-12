package prateek_gupta.SampleProject.prateek_gupta;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import prateek_gupta.SampleProject.utils.Util;

import java.lang.reflect.Method;
import java.util.*;

import static prateek_gupta.SampleProject.prateek_gupta.ProjectSettings.*;

public class Init {

    public static Map<String,Object> configuration_properties=new HashMap<>();


    static {
        try {
            preConstructMethodExecution();
        } catch (Exception e) {
            ServiceException.logException(e);
        }
    }

    @PreConstructMethod
    public static void loadConfigPropertiesFromFile() throws Exception {
        Utils.loadPropertiesFromFile(
                configuration_properties_file_path,
                configuration_properties,
                ProjectSettings.required_fields,
                ProjectSettings.expected_fields);

    }

    public static Object getConfiguration(String key,String value){
        return configuration_properties.getOrDefault(key,value);
    }

    public static void preConstructMethodExecution() throws Exception {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage("prateek_gupta.SampleProject")
                .addScanners(Scanners.MethodsAnnotated));
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

    public static void postConstructMethodExecution() throws Exception {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage("prateek_gupta.SampleProject")
                .addScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PostConstructMethod.class);
        System.out.println("PostConstructMethods : "+methods);

        for (Method method:methods)
            method.invoke(Util.getClassObject(method),
                    method.getAnnotation(PostConstructMethod.class).value());

    }
}
