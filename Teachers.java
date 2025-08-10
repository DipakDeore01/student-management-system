import java.util.Scanner;

class Teachers {
    private String Username= "Teachers";
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
            System.out.println("Teachers, Login Successful");
        }else {
            System.out.println("Enter valid Username, Password.");
        }
    }

}