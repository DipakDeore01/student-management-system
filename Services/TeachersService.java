package Services;

import java.io.*;
import java.util.Scanner;

public class TeachersService {
    private static final String TEACHER_FILE = "src/Data/teachers.txt";
    private static final String SESSION_FILE = "src/Data/teacher_session.txt";
    private static final String STUDENT_FILE = "src/Data/students.txt";
    private static final String ATTENDANCE_FILE = "src/Data/attendance.txt";
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

                String[] data = line.split(",");
                if (data.length >= 8) {
                    String fileEmail = data[6].trim();
                    String filePassword = data[7].trim();

                    if (fileEmail.equalsIgnoreCase(email) && filePassword.equals(password)) {
                        System.out.println("\nLogin Successful! Welcome " + data[1]);
                        saveLoginSession(data[1], fileEmail);
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
            System.out.println("[1] View Profile\n" +
                    "[2] View Class Students\n" +
                    "[3] Mark Attendance\n" +
                    "[4] Enter Marks\n" +
                    "[5] View Timetable\n" +
                    "[6] View Attendance (All)\n" +
                    "[7] View Attendance by Roll No\n" +
                    "[8] Logout");


            System.out.print(">> Enter choice: ");
            byte choice = sc.nextByte();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewProfile(teacherName);
                case 2 -> viewStudents();
                case 3 -> markAttendance(teacherName);
                case 4 -> System.out.println("Feature: Enter Marks (coming soon)");
                case 5 -> System.out.println("Feature: View Timetable (coming soon)");
                case 6 -> viewAttendance();
                case 7 -> viewAttendanceByRollNo();
                case 8 -> {
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
                return data[0];
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

    public void viewStudents() {
        try {
            File file = new File("src/Data/students.txt");
            Scanner Reader = new Scanner(file);
            System.out.println();
            while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                System.out.println(data);
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No students found yet.");
        }
    }

    private void markAttendance(String teacherName) {
        System.out.println("\n+-------- Mark Attendance --------+");

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE));
             FileWriter fw = new FileWriter(ATTENDANCE_FILE, true)) {

            String line;
            String today = java.time.LocalDate.now().toString();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    String studentId = data[0].trim();
                    String studentName = data[1].trim();

                    System.out.print("Mark attendance for " + studentName + " (P/A): ");
                    String status = sc.nextLine().trim().toUpperCase();

                    if (!status.equals("P") && !status.equals("A")) {
                        status = "A"; // default absent if wrong input
                    }

                    fw.write(today + "," + studentId + "," + studentName + "," + status + "\n");
                }
            }

            System.out.println("\nAttendance marked successfully for date: " + today);

        } catch (IOException e) {
            System.out.println("Error while marking attendance.");
        }
    }


    private void viewAttendance() {
        System.out.println("\n+-------- View Attendance --------+");

        File file = new File(ATTENDANCE_FILE);
        if (!file.exists()) {
            System.out.println("No attendance records found!");
            return;
        }

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

                    System.out.printf("%-12s %-8s %-20s %-10s%n", date, id, name, status);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance records.");
        }
    }


    private void viewAttendanceByRollNo() {
        System.out.println("\n+-------- View Attendance by Roll No --------+");

        File file = new File(ATTENDANCE_FILE);
        if (!file.exists()) {
            System.out.println("No attendance records found!");
            return;
        }

        System.out.print("Enter Student Roll No: ");
        String rollNo = sc.nextLine().trim();

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

                    if (id.equals(rollNo)) {
                        System.out.printf("%-12s %-8s %-20s %-10s%n", date, id, name, status);
                        found = true;
                    }
                }
            }

            if (!found) {
                System.out.println("No attendance found for Roll No: " + rollNo);
            }

        } catch (IOException e) {
            System.out.println("Error reading attendance records.");
        }
    }

}
