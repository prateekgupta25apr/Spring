package SolarSystem;

import org.springframework.context.support.ClassPathXmlApplicationContext;

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
