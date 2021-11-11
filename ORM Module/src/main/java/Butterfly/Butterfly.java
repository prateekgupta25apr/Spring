package Butterfly;

import Butterfly.dto.DTO;
import Butterfly.service.Service;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class Butterfly {
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        byte userChoice;
        char userWish;
        do {
            System.out.println("Enter 1. To save details of a Butterfly 2. To get a Butterfly's details by id");
            userChoice = scanner.nextByte();
            scanner.nextLine();
            switch (userChoice) {
                case 1 :
                    DTO dto=new DTO();
                    System.out.println("Enter id of the Butterfly");
                    dto.setId(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("Enter species name of the butterfly");
                    dto.setSpeciesName(scanner.nextLine());
                    System.out.println("Enter wing size of the Butterfly");
                    dto.setWingSize(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("Enter wing color of the butterfly");
                    dto.setWingColor(scanner.nextLine());
                    System.out.println("Enter age of the Butterfly");
                    dto.setAge(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("Enter origin of the Butterfly");
                    dto.setOrigin(scanner.nextLine());
                    System.out.println("Enter antennae size of the Butterfly");
                    dto.setAntennaeSize(scanner.nextInt());
                    scanner.nextLine();
                    Service service = (Service) new ClassPathXmlApplicationContext(
                            "butterfly.xml").getBean("service");
                    System.out.println(service.save(dto));
                
                case 2:
                    Service service2 = (Service) new ClassPathXmlApplicationContext(
                            "butterfly.xml").getBean("service");
                    System.out.println("Enter id of the Butterfly");
                    service2.getById(scanner.nextInt());
                    scanner.nextLine();
                
            }
            System.out.println("Enter Y/y to continue or any other character to exit");
            userWish = scanner.nextLine().charAt(0);
        } while (userWish == 'Y' || userWish == 'y');
    }
}
