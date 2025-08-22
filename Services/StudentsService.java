package Services;

import java.io.*;
import java.util.Scanner;

public class StudentsService {
    private static final String STUDENT_FILE = "src/Data/students.txt";
    private static final String SESSION_FILE = "src/Data/student_session.txt";
    Scanner sc = new Scanner(System.in);

    public void start() {
        if (isLoggedIn()) {
            String studentName = getLoggedInStudentName();
            System.out.println("\nWelcome back, " + studentName + "!");
            showDashboard(studentName);
        } else {
            login();
        }
    }

    public void login() {
        System.out.println("+-------- Student Login --------+");

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // File format: Name,Course,Year,Phone,Email,Password
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String fileEmail = data[4].trim();
                    String filePassword = data[5].trim();

                    if (fileEmail.equalsIgnoreCase(email) && filePassword.equals(password)) {
                        System.out.println("\nLogin Successful! Welcome " + data[0]);
                        saveLoginSession(data[0], fileEmail); // Save session
                        found = true;
                        showDashboard(data[0]);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading students file.");
        }

        if (!found) {
            System.out.println("Invalid Email or Password. Try again!");
        }
    }

    private void showDashboard(String studentName) {
        while (true) {
            System.out.println("\n+-------- Student Dashboard --------+");
            System.out.println("Hello, " + studentName + "!");
            System.out.println("[1] View Profile");
            System.out.println("[2] Logout");

            System.out.print(">> Enter choice: ");
            byte choice = sc.nextByte();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewProfile(studentName);
                case 2 -> {
                    clearLoginSession();
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void viewProfile(String studentName) {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[0].equals(studentName)) {
                    System.out.println("\n+-------- Student Profile --------+");
                    System.out.println("Name: " + data[0]);
                    System.out.println("Course: " + data[1]);
                    System.out.println("Year: " + data[2]);
                    System.out.println("Phone: " + data[3]);
                    System.out.println("Email: " + data[4]);
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

    private String getLoggedInStudentName() {
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line = br.readLine();
            if (line != null) {
                String[] data = line.split(",");
                return data[0]; // return student name
            }
        } catch (IOException e) {
            return "Student";
        }
        return "Student";
    }

    private void clearLoginSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
