package Services;

import Model.Admin;
import java.util.Scanner;

public class AdminService {
    Scanner sc = new Scanner(System.in);
    Admin admin = new Admin();
    public void login(){
        System.out.print("Email: ");
        String user= sc.nextLine();
        System.out.print("Password: ");
        String pass= sc.nextLine();
        if (admin.getEmail().equals(user)&& admin.getPassword().equals(pass)){
            System.out.println("Admin, Login Successful");
        }else {
            System.out.println("Enter valid Username, Password.");
        }
    }


}
