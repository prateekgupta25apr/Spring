package prateek_gupta.spring.bean.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public AnnotationBasedBean myBean() {
        return new AnnotationBasedBean();
    }

    @Bean
    public String name() {
        return "ExampleBean";
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext=
                new AnnotationConfigApplicationContext(Configuration.class);

        AnnotationBasedBean annotationBasedBean =
                applicationContext.getBean(AnnotationBasedBean.class);

        System.out.println(annotationBasedBean);
    }
}
