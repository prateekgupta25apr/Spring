package SolarSystem;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
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
        new ClassPathXmlApplicationContext("solarSystem.xml")
                .getBean("solarSystem");
    }
}
