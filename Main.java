import Services.AdminService;
import Services.TeachersService;
import Services.StudentsService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AdminService adminService = new AdminService();
        TeachersService teachersService = new TeachersService();
        StudentsService studentsService = new StudentsService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n+---------------------------------------+");
            System.out.println("|    Student Management System (CLI)    |");
            System.out.println("+---------------------------------------+\n");

            System.out.println("[1] Admin Login");
            System.out.println("[2] Teachers Login");
            System.out.println("[3] Student Login");
            System.out.println("[0] Exit");

            System.out.print(">> Enter your choice: ");
            byte choice;
            try {
                choice = sc.nextByte();
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> adminService.start();
                case 2 -> teachersService.login();
                case 3 -> studentsService.login();
                case 0 -> {
                    System.out.println("Exiting System... Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
