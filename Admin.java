import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Admin{
    private String Username= "Admin";
    private String Password= "1234";

    Scanner sc= new Scanner(System.in);

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public void login(){
        System.out.print("Username: ");
        String user= sc.nextLine();
        System.out.print("Password: ");
        String pass= sc.nextLine();
        if (getUsername().equals(user)&& getPassword().equals(pass)){
            System.out.println("Admin, Login Successful");
        }else {
            System.out.println("Enter valid Username, Password.");
        }
    }

    ArrayList<String> students= new ArrayList<>();

    public void add(String id, String name, String course, String year) {
        String studentData = id + "," + name + "," + course + "," + year;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt", true));
            writer.write(studentData);
            writer.newLine();
            writer.close();

            System.out.println("Student data added successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void viewstd(){

    }

}