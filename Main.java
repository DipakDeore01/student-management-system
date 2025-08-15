import Services.AdminService;
import Services.TeachersService;
import Services.StudentsService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AdminService adminService= new AdminService();
        TeachersService teachersService = new TeachersService();
        StudentsService studentsService= new StudentsService();
        Scanner sc= new Scanner(System.in);
        System.out.println("+---------------------------------------+\n" +
                         "|    Student Management System (CLI)    |\n" +
                         "+---------------------------------------+\n");

        System.out.println("[1] Admin Login \n" +
                "[2] Teachers Login \n" +
                "[3] Student Login \n" +
                "[0] Exit");
        System.out.print(">> Enter your choice: ");
        byte choice= sc.nextByte();
        switch (choice){
            case 1:
                adminService.start();
                break;
            case 2:
                teachersService.login();
                break;
            case 3:
                studentsService.login();
                break;
            case 0:
                break;
        }
    }
}