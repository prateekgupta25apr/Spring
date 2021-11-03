package State;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class State {
    String name;
    City city;

    public State(String name, City city) {
        System.out.println("State constructor called");
        this.name = name;
        this.city = city;
    }

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("state.xml").getBean("state");
    }
}
