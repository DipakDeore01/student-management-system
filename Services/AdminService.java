package Services;

import Model.Admin;
import Model.Students;
import Model.Teachers;

import java.io.*;
import java.util.Scanner;

public class AdminService {

    Scanner sc = new Scanner(System.in);
    Admin admin = new Admin();
    Students students = new Students();
    Teachers teachers = new Teachers();

    private static final String SESSION_FILE = "src/Data/session.txt";

    public void start() {
        if (isLoggedIn()) {
            System.out.println("\nWelcome back, Admin!");
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
            System.out.print("Email: ");
            String user = sc.nextLine();

            System.out.print("Password: ");
            String pass = sc.nextLine();

            if (admin.getEmail().equals(user) && admin.getPassword().equals(pass)) {
                saveLoginSession();
                System.out.println("\nAdmin, Login Successful");
                showDashboard();
            } else {
                System.out.println("Enter valid Username, Password.");
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
            System.out.println("\n+-------- Admin Dashboard --------+\n" +
                    "\n[1] Add Student\n" +
                    "[2] View Students\n" +
                    "[3] Update Student\n" +
                    "[4] Delete Student\n" +
                    "[5] Add Teacher\n" +
                    "[6] View Teachers\n" +
                    "[7] Logout");
            System.out.print(">> Enter your choice: ");
            byte choice = sc.nextByte();
            sc.nextLine();

            switch (choice) {
                case 1 -> addStudents();
                case 2 -> viewStudents();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> addTeacher();
                case 6 -> viewTeachers();
                case 7 -> {
                    clearLoginSession();
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public String addStudents() {
        System.out.println("+-------- Student Details --------+");

        System.out.print("Name: ");
        students.setName(sc.nextLine());

        System.out.print("Course: ");
        students.setCourse(sc.nextLine());

        System.out.print("Year: ");
        students.setYear(sc.nextInt());
        sc.nextLine();

        System.out.print("Phone: ");
        students.setPhone(sc.nextLong());
        sc.nextLine();

        System.out.print("Email: ");
        students.setEmail(sc.nextLine());

        System.out.print("Password: ");
        students.setPassword(sc.nextLine());

        try {
            File dir = new File("src/Data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter("src/Data/students.txt", true); // append mode
            writer.write("\n" + students.getName() + "," +
                    students.getCourse() + "," +
                    students.getYear() + "," +
                    students.getPhone() + "," +
                    students.getEmail() + "," +
                    students.getPassword());
            writer.close();
            System.out.println("Student Added Successfully");
            return "Student Added Successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void updateStudent() {
        System.out.print("Enter Email of student to update: ");
        String emailToUpdate = sc.nextLine();

        File file = new File("src/Data/students.txt");
        File tempFile = new File("src/Data/students_temp.txt");

        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 6 && data[4].equalsIgnoreCase(emailToUpdate)) {
                    System.out.println("Updating Student: " + data[0]);

                    System.out.print("New Name: ");
                    data[0] = sc.nextLine();

                    System.out.print("New Course: ");
                    data[1] = sc.nextLine();

                    System.out.print("New Year: ");
                    data[2] = sc.nextLine();

                    System.out.print("New Phone: ");
                    data[3] = sc.nextLine();

                    System.out.print("New Email: ");
                    data[4] = sc.nextLine();

                    System.out.print("New Password: ");
                    data[5] = sc.nextLine();

                    line = String.join(",", data);
                    updated = true;
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.delete()) {
            tempFile.renameTo(file);
        }

        if (updated) {
            System.out.println("Student Updated Successfully!");
        } else {
            System.out.println("Student not found with Email: " + emailToUpdate);
        }
    }

    public void deleteStudent() {
        System.out.print("Enter Email of student to delete: ");
        String emailToDelete = sc.nextLine();

        File file = new File("src/Data/students.txt");
        File tempFile = new File("src/Data/students_temp.txt");

        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 6 && data[4].equalsIgnoreCase(emailToDelete)) {
                    System.out.println("Deleting Student: " + data[0]);
                    deleted = true;
                    continue; // skip writing this student
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.delete()) {
            tempFile.renameTo(file);
        }

        if (deleted) {
            System.out.println("Student Deleted Successfully!");
        } else {
            System.out.println("Student not found with Email: " + emailToDelete);
        }
    }

    public String addTeacher() {
        System.out.println("+-------- Teacher Details --------+");

        System.out.print("Id: ");
        teachers.setId(sc.nextLine());

        System.out.print("Name: ");
        teachers.setName(sc.nextLine());

        System.out.print("Qualifications: ");
        teachers.setQualifications(sc.nextLine());

        System.out.print("Department: ");
        teachers.setDepartment(sc.nextLine());

        System.out.print("Subject: ");
        teachers.setSubject(sc.nextLine());

        System.out.print("Phone : ");
        teachers.setPhoneno(sc.nextLong());
        sc.nextLine();

        System.out.print("Email: ");
        teachers.setEmail(sc.nextLine());

        System.out.print("Password: ");
        teachers.setPassword(sc.nextLine());

        try {
            File dir = new File("src/Data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter("src/Data/teachers.txt", true); // append mode
            writer.write("\n" + teachers.getId() + "," +
                    teachers.getName() + "," +
                    teachers.getQualifications() + "," +
                    teachers.getDepartment() + "," +
                    teachers.getSubject() + "," +
                    teachers.getPhoneno() + "," +
                    teachers.getEmail() + "," +
                    teachers.getPassword()
            );
            writer.close();
            System.out.println("Teacher Added Successfully");
            return "Teacher Added Successfully";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewTeachers() {
        try {
            File file = new File("src/Data/teachers.txt");
            Scanner Reader = new Scanner(file);
            System.out.println();
            while (Reader.hasNextLine()) {
                String data = Reader.nextLine();
                System.out.println(data);
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No teachers found yet.");
        }
    }
}
