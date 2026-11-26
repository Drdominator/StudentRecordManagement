
package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== STUDENT RECORD MANAGEMENT =====");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();

        if (choice == 1) {
            addStudent(sc);
        }
        else if (choice == 2) {
            viewStudents();
        }
        else if (choice == 3) {
            searchStudent(sc);
        }
        else if (choice == 4) {
            updateStudent(sc);
        }
        else if (choice == 5) {
            System.out.println("Thank you!");
        }
        else {
            System.out.println("Invalid choice!");
        }

        sc.close();
    }

    // ADD STUDENT
    public static void addStudent(Scanner sc) {

        try {
            System.out.print("Enter Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();

            String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, course);
                ps.setString(4, email);
                ps.setDouble(5, marks);

                ps.executeUpdate();

                System.out.println("Student added successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // VIEW ALL STUDENTS
    public static void viewStudents() {

        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== ALL STUDENTS =====");

            while (rs.next()) {
                System.out.println("----------------------");
                System.out.println("ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Course: " + rs.getString("course"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Marks: " + rs.getDouble("marks"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SEARCH STUDENT
    public static void searchStudent(Scanner sc) {

        System.out.print("Enter Student ID to search: ");
        int id = sc.nextInt();

        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println("\n===== STUDENT FOUND =====");
                    System.out.println("ID: " + rs.getInt("student_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Course: " + rs.getString("course"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Marks: " + rs.getDouble("marks"));
                }
                else {
                    System.out.println("Student not found!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE STUDENT
    public static void updateStudent(Scanner sc) {

        System.out.print("Enter Student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Name: ");
        String name = sc.nextLine();

        System.out.print("Enter New Course: ");
        String course = sc.nextLine();

        System.out.print("Enter New Email: ");
        String email = sc.nextLine();

        System.out.print("Enter New Marks: ");
        double marks = sc.nextDouble();

        String sql = "UPDATE students SET name=?, course=?, email=?, marks=? WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, course);
            ps.setString(3, email);
            ps.setDouble(4, marks);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            }
            else {
                System.out.println("Student ID not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}