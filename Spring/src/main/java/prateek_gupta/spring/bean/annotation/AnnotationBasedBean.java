package prateek_gupta.spring.bean.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnnotationBasedBean implements InitializingBean, DisposableBean {
    private String name;

    // Default constructor
    public AnnotationBasedBean() {
        System.out.println("1. Constructor: AnnotationBasedBean is instantiated.");
    }

    // Setter method for dependency injection
    @Autowired
    public void setName(String name) {
        this.name = name;
        System.out.println("2. Property Setter: Name is set to " + name);
    }

    // InitializingBean method
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("3. InitializingBean: Properties are set.");
    }

    // Custom initialization method
    public void customInit() {
        System.out.println("4. Custom Init Method: AnnotationBasedBean is initialized.");
    }

    // DisposableBean method
    @Override
    public void destroy() throws Exception {
        System.out.println("5. DisposableBean: AnnotationBasedBean is being destroyed.");
    }

    // Custom destroy method
    public void customDestroy() {
        System.out.println("6. Custom Destroy Method: AnnotationBasedBean is destroyed.");
    }

    @Override
    public String toString() {
        return "AnnotationBasedBean{name='" + name + "'}";
    }
}
