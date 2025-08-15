package Services;

import Model.Admin;
import Model.Students;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminService {

    Scanner sc = new Scanner(System.in);
    Admin admin = new Admin();
    Students students = new Students();

    private static final String SESSION_FILE = "src/Data/session.txt";

    public void start() {
        // First check if already logged in
        if (isLoggedIn()) {
            System.out.println("Welcome back, Admin!");
            showDashboard();
        } else {
            login();
        }
    }

    private boolean isLoggedIn() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                return "loggedin".equalsIgnoreCase(line);
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public void login() {
        while (true) {
            System.out.print("Email: ");
            String user = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();
            if (admin.getEmail().equals(user) && admin.getPassword().equals(pass)) {
                saveLoginSession();
                System.out.println("Admin, Login Successful\n");
                showDashboard();
                break;
            } else {
                System.out.println("Enter valid Username, Password.");
            }
        }
    }

    private void saveLoginSession() {
        try {
            File dir = new File("src/Data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(SESSION_FILE);
            writer.write("loggedin");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearLoginSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    private void showDashboard() {
        while (true) {
            System.out.println("+-------- Admin Dashboard --------+\n" +
                    "[1] Add Student\n" +
                    "[2] View Students\n" +
                    "[3] Update Student\n" +
                    "[4] Delete Student\n" +
                    "[5] Add Teacher\n" +
                    "[6] View Teachers\n" +
                    "[7] Generate Reports\n" +
                    "[8] Logout");
            System.out.print(">> Enter your choice: ");
            byte choice = sc.nextByte();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addStudents();
                case 8 -> {
                    clearLoginSession();
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    List<Students> list = new ArrayList<>();

    public String addStudents() {
        System.out.println("+-------- Student Details --------+");

        System.out.print("Name: ");
        students.setName(sc.nextLine());

        System.out.print("Course: ");
        students.setCourse(sc.nextLine());

        System.out.print("Year: ");
        students.setYear(sc.nextInt());
        sc.nextLine(); // consume leftover newline

        System.out.print("Phone: ");
        students.setPhone(sc.nextLong());
        sc.nextLine(); // consume leftover newline

        System.out.print("Email: ");
        students.setEmail(sc.nextLine());

        System.out.print("Password: ");
        students.setPassword(sc.nextLine());

        try {
            File dir = new File("src/Data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter("src/Data/students.txt", true); // append mode
            writer.write(students.getName() + "," +
                    students.getCourse() + "," +
                    students.getYear() + "," +
                    students.getPhone() + "," +
                    students.getEmail() + "," +
                    students.getPassword() + "\n");
            writer.close();
            System.out.println("Student Added Successfully");
            return "Student Added Successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
