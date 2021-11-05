package Planet;

import Planet.dto.DTO;
import Planet.service.Service;
import Planet.service.ServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class Planet {
    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);
        byte userChoice;
        char userWish;
        do {
            System.out.println("""
                    Enter
                    1. To add details of a planet
                    2. To get details of a planet by id
                    3. To update details of a planet
                    4. To delete details of a planet""");
            userChoice=scanner.nextByte();
            scanner.nextLine();
            switch (userChoice){
                case 1->{
                    DTO planet=new DTO();
                    System.out.println("Enter id for the planet");
                    planet.setId(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("Enter name of the planet");
                    planet.setName(scanner.nextLine());
                    Service service=(Service) new ClassPathXmlApplicationContext(
                            "planet.xml").getBean("service");
                    System.out.println(service.save(planet));
                }
                case 2->{

                    Service service=(Service) new ClassPathXmlApplicationContext(
                            "planet.xml").getBean("service");
                    System.out.println("Enter id for the planet");
                    service.getById(scanner.nextInt());
                    scanner.nextLine();
                }
                case 3->{
                    Service service=(Service) new ClassPathXmlApplicationContext(
                            "planet.xml").getBean("service");
                    DTO planet=new DTO();
                    System.out.println("Enter id for the planet");
                    planet.setId(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("Enter name of the planet");
                    planet.setName(scanner.nextLine());
                    System.out.println(service.update(planet));
                }
                case 4->{
                    Service service=(Service) new ClassPathXmlApplicationContext(
                            "planet.xml").getBean("service");
                    System.out.println("Enter id for the planet");
                    System.out.println(service.delete(scanner.nextInt()));
                    scanner.nextLine();
                }
            }
            System.out.println("Enter Y/y to continue or any other character to exit");
            userWish=scanner.nextLine().charAt(0);
        }while (userWish=='Y'||userWish=='y');
    }
}
