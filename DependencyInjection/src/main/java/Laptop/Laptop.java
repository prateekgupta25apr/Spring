package Laptop;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Laptop {
    String brand;
    OS osName;

    public Laptop(String brand, OS osName) {
        System.out.println("Laptop constructor called");
        this.brand = brand;
        this.osName = osName;
    }

    public static void main(String[] args) {
        Resource resource=new ClassPathResource("laptop.xml");
        BeanFactory factory=new XmlBeanFactory(resource);
        factory.getBean("laptop");
    }
}
