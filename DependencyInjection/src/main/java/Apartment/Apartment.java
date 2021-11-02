package Apartment;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Apartment {
    String city;
    Flat flatNo;

    public Apartment() {
        System.out.println("Apartment constructor called");
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFlatNo(Flat flatNo) {
        this.flatNo = flatNo;
    }

    public static void main(String[] args) {
        Resource resource=new ClassPathResource("apartment.xml");
        BeanFactory factory=new XmlBeanFactory(resource);
        factory.getBean("apartment");
    }
}
