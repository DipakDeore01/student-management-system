package Services;

import Model.Teachers;

import java.util.Scanner;

public class TeachersService {
    Scanner sc = new Scanner(System.in);
    Teachers teachers = new Teachers();

    public void login(){
        System.out.print("Email: ");
        String user= sc.nextLine();
        System.out.print("Password: ");
        String pass= sc.nextLine();
        if (teachers.getEmail().equals(user)&& teachers.getPassword().equals(pass)){
            System.out.println("Admin.Admin, Login Successful");
        }else {
            System.out.println("Enter valid Username, Password.");
        }
    }
}
