package State;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class State {
    String name;
    City city;

    public State(String name, City city) {
        System.out.println("State constructor called");
        this.name = name;
        this.city = city;
    }

    public static void main(String[] args) {
        Resource resource=new ClassPathResource("state.xml");
        BeanFactory factory=new XmlBeanFactory(resource);
        factory.getBean("state");
    }
}
