package Services;

import java.io.*;
import java.util.Scanner;

public class TeachersService {
    private static final String TEACHER_FILE = "src/Data/teachers.txt";
    private static final String SESSION_FILE = "src/Data/teacher_session.txt";
    Scanner sc = new Scanner(System.in);

    public void start() {
        if (isLoggedIn()) {
            String teacherName = getLoggedInTeacherName();
            System.out.println("\nWelcome back, " + teacherName + "!");
            showDashboard(teacherName);
        } else {
            login();
        }
    }

    public void login() {
        System.out.println("+-------- Teacher Login --------+");

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(TEACHER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // File format: Id,Name,Qualifications,Department,Subject,Phone,Email,Password
                String[] data = line.split(",");
                if (data.length >= 8) {
                    String fileEmail = data[6].trim();
                    String filePassword = data[7].trim();

                    if (fileEmail.equalsIgnoreCase(email) && filePassword.equals(password)) {
                        System.out.println("\nLogin Successful! Welcome " + data[1]);
                        saveLoginSession(data[1], fileEmail); // Save session
                        found = true;
                        showDashboard(data[1]);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading teachers file.");
        }

        if (!found) {
            System.out.println("Invalid Email or Password. Try again!");
        }
    }

    private void showDashboard(String teacherName) {
        while (true) {
            System.out.println("\n+-------- Teacher Dashboard --------+");
            System.out.println("Hello, " + teacherName + "!");
            System.out.println("[1] View Profile");
            System.out.println("[2] Logout");

            System.out.print(">> Enter choice: ");
            byte choice = sc.nextByte();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewProfile(teacherName);
                case 2 -> {
                    clearLoginSession();
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void viewProfile(String teacherName) {
        try (BufferedReader br = new BufferedReader(new FileReader(TEACHER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8 && data[1].equals(teacherName)) {
                    System.out.println("\n+-------- Teacher Profile --------+");
                    System.out.println("Id: " + data[0]);
                    System.out.println("Name: " + data[1]);
                    System.out.println("Qualifications: " + data[2]);
                    System.out.println("Department: " + data[3]);
                    System.out.println("Subject: " + data[4]);
                    System.out.println("Phone: " + data[5]);
                    System.out.println("Email: " + data[6]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading profile.");
        }
    }

    private void saveLoginSession(String name, String email) {
        try {
            File dir = new File("src/Data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(SESSION_FILE);
            writer.write(name + "," + email);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLoggedIn() {
        File file = new File(SESSION_FILE);
        return file.exists();
    }

    private String getLoggedInTeacherName() {
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line = br.readLine();
            if (line != null) {
                String[] data = line.split(",");
                return data[0]; // return teacher name
            }
        } catch (IOException e) {
            return "Teacher";
        }
        return "Teacher";
    }

    private void clearLoginSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
