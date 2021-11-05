package Laptop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Getter
@Setter
@ToString
public class Laptop {
    String brandName;
    int version;
    OS osName;

    public static void main(String[] args) {

        System.out.println("Autowire Constructor: " +
                new ClassPathXmlApplicationContext("laptop.xml")
                        .getBean("laptopAutowireConstructor"));

        System.out.println("Autowire ByName: " +
                new ClassPathXmlApplicationContext("laptop.xml")
                        .getBean("laptopAutowireByName"));

        System.out.println("Property tags: " +
                new ClassPathXmlApplicationContext("laptop.xml")
                        .getBean("laptopProperty"));
    }

    public Laptop() {
    }

    public Laptop(String brandName, int version, OS osName) {
        this.brandName = brandName;
        this.version = version;
        this.osName = osName;
    }
}
