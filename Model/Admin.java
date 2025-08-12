package Model;

import java.util.Scanner;

public class Admin{
    private String email= "admin@mail.com";
    private String password= "123456";

    Scanner sc= new Scanner(System.in);

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}