package Car;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Car {
    String company;
    Engine engineType;

    public Car() {
        System.out.println("Car coolant called");
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setEngineType(Engine engineType) {
        this.engineType = engineType;
    }

    public static void main(String[] args) {
        Resource resource=new ClassPathResource("car.xml");
        BeanFactory factory=new XmlBeanFactory(resource);
        factory.getBean("car");
    }
}
