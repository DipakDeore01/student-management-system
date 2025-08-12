package Services;

import java.util.Scanner;
import Model.Students;

public class StudentsService {
    Scanner sc= new Scanner(System.in);
    Students students= new Students();

    public void login(){
        System.out.print("Username: ");
        String user= sc.nextLine();
        System.out.print("Password: ");
        String pass= sc.nextLine();
        if (students.getEmail().equals(user)&& students.getPassword().equals(pass)){
            System.out.println("Admin.Admin, Login Successful");
        }else {
            System.out.println("Enter valid Username, Password.");
        }
    }
}
