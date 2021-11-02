package SolarSystem;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SolarSystem {
    int numberOfPlanet;
    Planet planet;

    public SolarSystem() {
        System.out.println("SolarSystem constructor called");
    }

    public void setNumberOfPlanet(int numberOfPlanet) {
        this.numberOfPlanet = numberOfPlanet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public static void main(String[] args) {
        Resource resource=new ClassPathResource("solarSystem.xml");
        BeanFactory factory=new XmlBeanFactory(resource);
        factory.getBean("solarSystem");
    }
}
