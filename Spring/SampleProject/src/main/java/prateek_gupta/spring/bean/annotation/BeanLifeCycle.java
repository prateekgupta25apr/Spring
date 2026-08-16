package prateek_gupta.spring.bean.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
public class BeanLifeCycle {
    @Bean(initMethod = "customInit",
            destroyMethod = "customDestroy")
    public AnnotationBasedBean annotationBasedBean() {
        return new AnnotationBasedBean();
    }

    @Bean
    public String name() {
        return "Value for name attribute";
    }

    public static void main(String[] args) {
        // Creating an IoC container with the BeanLifeCycle class as the configuration
        // class(as it's passed in the constructor)
        ApplicationContext applicationContext=
                new AnnotationConfigApplicationContext(BeanLifeCycle.class);

        // Creating a bean of AnnotationBasedBean
        AnnotationBasedBean annotationBasedBean =
                applicationContext.getBean(AnnotationBasedBean.class);

        // Validating class details
        System.out.println(annotationBasedBean);

        // Shutting down the IoC Container
        //noinspection CastCanBeRemovedNarrowingVariableType
        ((AnnotationConfigApplicationContext)applicationContext).close();
    }
}
