package Services;

import java.io.*;
import java.util.Scanner;

public class StudentsService {
    private static final String STUDENT_FILE = "src/Data/students.txt";
    private static final String SESSION_FILE = "src/Data/student_session.txt";
    private static final String ATTENDANCE_FILE = "src/Data/attendance.txt";
    Scanner sc = new Scanner(System.in);
    private String studentRollNo;

    public void start() {
        if (isLoggedIn()) {
            studentRollNo = getLoggedInStudentRollNo();
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

                String[] data = line.split(",");
                if (data.length >= 7) {
                    String fileRollNo = data[0].trim();
                    String fileName = data[1].trim();
                    String fileEmail = data[5].trim();
                    String filePassword = data[6].trim();

                    if (fileEmail.equalsIgnoreCase(email) && filePassword.equals(password)) {
                        System.out.println("\nLogin Successful! Welcome " + fileName);
                        saveLoginSession(fileRollNo, fileName, fileEmail);
                        found = true;
                        studentRollNo = fileRollNo;
                        showDashboard(fileName);
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
            System.out.println("[1] View Profile\n" +
                    "[2] View Attendance\n" +
                    "[3] View Results\n" +
                    "[4] View Timetable\n" +
                    "[5] Logout");

            System.out.print(">> Enter choice: ");
            byte choice = sc.nextByte();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewProfile();
                case 2 -> viewMyAttendance(studentRollNo, studentName);
                case 3 -> System.out.println("Feature: View Result (coming soon)");
                case 4 -> System.out.println("Feature: View Timetable (coming soon)");
                case 5 -> {
                    clearLoginSession();
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void viewProfile() {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7 && data[0].equals(studentRollNo)) {
                    System.out.println("\n+-------- Student Profile --------+");
                    System.out.println("Roll No: " + data[0]);
                    System.out.println("Name: " + data[1]);
                    System.out.println("Course: " + data[2]);
                    System.out.println("Year: " + data[3]);
                    System.out.println("Phone: " + data[4]);
                    System.out.println("Email: " + data[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading profile.");
        }
    }

    private void saveLoginSession(String rollNo, String name, String email) {
        try {
            File dir = new File("src/Data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(SESSION_FILE);
            writer.write(rollNo + "," + name + "," + email);
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
                return data[1];
            }
        } catch (IOException e) {
            return "Student";
        }
        return "Student";
    }

    private String getLoggedInStudentRollNo() {
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line = br.readLine();
            if (line != null) {
                String[] data = line.split(",");
                return data[0];
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private void clearLoginSession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    private void viewMyAttendance(String studentRollNo, String studentName) {
        System.out.println("\n+-------- My Attendance --------+");

        File file = new File(ATTENDANCE_FILE);
        if (!file.exists()) {
            System.out.println("No attendance records found!");
            return;
        }

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.printf("%-12s %-8s %-20s %-10s%n", "Date", "ID", "Name", "Status");
            System.out.println("------------------------------------------------------");

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length == 4) {
                    String date = data[0];
                    String id = data[1];
                    String name = data[2];
                    String status = data[3];
                    if (id.equals(studentRollNo)) {
                        System.out.printf("%-12s %-8s %-20s %-10s%n", date, id, name, status);
                        found = true;
                    }
                }
            }
            if (!found) {
                System.out.println("No attendance found for Roll No: " + studentRollNo);
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance records.");
        }
    }

}
