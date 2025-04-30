package prateek_gupta.SampleProject.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Connecting to DB
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/jdbc", "root", "root");

            // Initializing src.java.Dao
            Dao dao = new DaoImpl();

            // Initializing Scanner
            Scanner scanner = new Scanner(System.in);

            byte userChoice;
            char userWish;
            Table1 table1;
            do {
                System.out.println(
                        "Enter code for action :\n"+
                        "1. Add data\n"+
                        "2. Read all data\n"+
                        "3. Update by id\n"+
                        "4. Delete by id\n");
                userChoice= scanner.nextByte();
                scanner.nextLine();
                switch (userChoice){
                    case 1:
                        table1 = new Table1();
                        System.out.println("Enter id");
                        table1.setId(scanner.nextInt());
                        scanner.nextLine();
                        System.out.println("Enter name");
                        table1.setName(scanner.nextLine());
                        if (dao.save(connection, table1))
                            System.out.println("Saved data successfully");
                        else
                            System.out.println("Saving data failed");
                        break;

                    case 2:
                        dao.read(connection);
                        break;

                    case 3:
                        table1 = new Table1();
                        System.out.println("Enter id for which details to be updated");
                        table1.setId(scanner.nextInt());
                        scanner.nextLine();
                        System.out.println("Enter updated name");
                        table1.setName(scanner.nextLine());
                        if (dao.update(connection, table1))
                            System.out.println("Data updated successfully");
                        else
                            System.out.println("Data update failed");
                        break;
                    case 4:
                        table1 = new Table1();
                        System.out.println("Enter id for which details to be deleted");
                        table1.setId(scanner.nextInt());
                        scanner.nextLine();
                        if (dao.delete(connection, table1))
                            System.out.println("Data deleted successfully");
                        else
                            System.out.println("Data deletion failed");
                        break;
                }

                System.out.println("Enter Y/y to continue or any other letter to exit");
                userWish=scanner.nextLine().charAt(0);
            }while (userWish=='Y'||userWish=='y');

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
