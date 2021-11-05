package Facebook;

import Facebook.dto.LoginDTO;
import Facebook.dto.UserDTO;
import Facebook.service.Service;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

public class Facebook {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        byte userChoice;
        char userWish;
        do {
            System.out.println("""
                    Enter
                    1. To register a user
                    2. To login a user
                    3. To get a user details by id
                    4. To update password by id""");
            userChoice = scanner.nextByte();
            scanner.nextLine();
            switch (userChoice) {
                case 1 -> {
                    UserDTO dto = new UserDTO();
                    System.out.println("Enter id of the user");
                    dto.setId(scanner.nextInt());
                    scanner.nextLine();
                    System.out.println("Enter name of the user");
                    dto.setName(scanner.nextLine());
                    System.out.println("Enter email of the user");
                    dto.setEmail(scanner.nextLine());
                    System.out.println("Enter password");
                    dto.setPassword(scanner.nextLine());
                    System.out.println("Confirm password");
                    dto.setConfirmPassword(scanner.nextLine());
                    System.out.println("Enter date of birth of the user");
                    dto.setDob(scanner.nextLine());
                    System.out.println("Enter gender of the user");
                    dto.setGender(scanner.nextLine());
                    Service service = (Service) new ClassPathXmlApplicationContext(
                            "facebook.xml").getBean("service");
                    System.out.println(service.validateAndSave(dto));
                }
                case 2 -> {
                    LoginDTO dto = new LoginDTO();
                    System.out.println("Enter email of the user");
                    dto.setEmail(scanner.nextLine());
                    System.out.println("Enter password");
                    dto.setPassword(scanner.nextLine());
                    System.out.println("Confirm password");
                    dto.setConfirmPassword(scanner.nextLine());
                    Service service = (Service) new ClassPathXmlApplicationContext(
                            "facebook.xml").getBean("service");
                    System.out.println(service.validateAndLogin(dto));
                }
                case 3 -> {
                    System.out.println("Enter id of the user");
                    Service service = (Service) new ClassPathXmlApplicationContext(
                            "facebook.xml").getBean("service");
                    System.out.println(service.getById(scanner.nextInt()));
                    scanner.nextLine();
                }
                case 4 -> {
                    System.out.println("Enter id of the user");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter updated password");
                    Service service = (Service) new ClassPathXmlApplicationContext(
                            "facebook.xml").getBean("service");
                    System.out.println(service.updatePassword(scanner.nextLine(), id));
                }
            }
            System.out.println("Enter Y/y to continue or any other character to exit");
            userWish = scanner.nextLine().charAt(0);
        } while (userWish == 'Y' || userWish == 'y');
    }
}
