import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Admin admin= new Admin();
        Teachers teachers = new Teachers();
        Student student= new Student();
        Scanner sc= new Scanner(System.in);
        System.out.println("+---------------------------------------+\n" +
                         "|    Student Management System (CLI)    |\n" +
                         "+---------------------------------------+\n");

        System.out.println("[1] Admin Login \n[2] Teachers Login \n[3] Student Login \n[0] Exit\n");
        System.out.print(">> Enter your choice: ");
        byte choice= sc.nextByte();
        switch (choice){
            case 1:
                admin.login();
                break;
            case 2:
                teachers.login();
                break;
            case 3:
                student.login();
                break;
            case 0:
                break;
        }
    }
}