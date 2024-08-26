package prateek_gupta.spring.bean.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnnotationBasedBean implements BeanNameAware, BeanFactoryAware,
        InitializingBean, DisposableBean {
    private String name;

    // Default constructor
    public AnnotationBasedBean() {
        System.out.println("1. Constructor: MyBean is instantiated.");
    }

    // Setter method for dependency injection
    @Autowired
    public void setName(String name) {
        this.name = name;
        System.out.println("2. Property Setter: Name is set to " + name);
    }

    // BeanNameAware method
    @Override
    public void setBeanName(String name) {
        System.out.println("3. BeanNameAware: Bean name is " + name);
    }

    // BeanFactoryAware method
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("4. BeanFactoryAware: BeanFactory is set.");
    }

    // InitializingBean method
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("5. InitializingBean: Properties are set.");
    }

    // Custom initialization method
    public void customInit() {
        System.out.println("6. Custom Init Method: MyBean is initialized.");
    }

    // DisposableBean method
    @Override
    public void destroy() throws Exception {
        System.out.println("7. DisposableBean: MyBean is being destroyed.");
    }

    // Custom destroy method
    public void customDestroy() {
        System.out.println("8. Custom Destroy Method: MyBean is destroyed.");
    }

    @Override
    public String toString() {
        return "AnnotationBasedBean{name='" + name + "'}";
    }
}
