
package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;
import java.time.LocalDateTime;

public class StudentManagementGUI extends JFrame {

    private JTextField idField, nameField, courseField;
    private JTextField emailField, marksField, searchField;

    private JTable table;
    private DefaultTableModel model;

    private JLabel statusLabel, totalLabel;

    private final Color NAVY = new Color(22, 40, 75);
    private final Color BLUE = new Color(42, 105, 190);
    private final Color BG = new Color(240, 244, 250);
    private final Color GREEN = new Color(35, 145, 95);
    private final Color RED = new Color(195, 65, 65);

    // CONSTRUCTOR
    public StudentManagementGUI() {

        setTitle("Student Record Management System");
        setSize(1150, 750);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createInterface();
        loadStudents();
    }

    // CREATE GUI
    private void createInterface() {

        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("STUDENT RECORD MANAGEMENT SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 23));

        JLabel databaseStatus = new JLabel("● MySQL Database");
        databaseStatus.setForeground(Color.GREEN);
        databaseStatus.setFont(new Font("Arial", Font.BOLD, 14));

        header.add(title, BorderLayout.WEST);
        header.add(databaseStatus, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // MAIN PANEL
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // SIDEBAR
        JPanel sidebar = new JPanel(new GridLayout(9, 1, 10, 12));
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(185, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        JLabel logo = new JLabel("SRMS", JLabel.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 25));

        sidebar.add(logo);

        JButton addMenu = createButton("Add Student", BLUE);
        JButton viewMenu = createButton("View Students", NAVY);
        JButton searchMenu = createButton("Search Student", NAVY);
        JButton updateMenu = createButton("Update Student", NAVY);
        JButton deleteMenu = createButton("Delete Student", NAVY);
        JButton importMenu = createButton("Import CSV", GREEN);
        JButton exportMenu = createButton("Export CSV", BLUE);
        JButton exitMenu = createButton("Exit", RED);

        sidebar.add(addMenu);
        sidebar.add(viewMenu);
        sidebar.add(searchMenu);
        sidebar.add(updateMenu);
        sidebar.add(deleteMenu);
        sidebar.add(importMenu);
        sidebar.add(exportMenu);
        sidebar.add(exitMenu);

        main.add(sidebar, BorderLayout.WEST);

        // CONTENT PANEL
        JPanel content = new JPanel(new BorderLayout(12, 15));
        content.setBackground(BG);

        // STUDENT FORM
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);

        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225)),
                "Student Information"
        ));

        idField = new JTextField(20);
        nameField = new JTextField(20);
        courseField = new JTextField(20);
        emailField = new JTextField(20);
        marksField = new JTextField(20);

        addFormRow(form, 0, "Student ID:", idField);
        addFormRow(form, 1, "Name:", nameField);
        addFormRow(form, 2, "Course:", courseField);
        addFormRow(form, 3, "Email:", emailField);
        addFormRow(form, 4, "Marks:", marksField);

        // FORM BUTTONS
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        actions.setBackground(BG);

        JButton addButton = createButton("Add Student", BLUE);
        JButton updateButton = createButton("Update", GREEN);
        JButton deleteButton = createButton("Delete", RED);
        JButton clearButton = createButton("Clear", NAVY);

        actions.add(addButton);
        actions.add(updateButton);
        actions.add(deleteButton);
        actions.add(clearButton);

        JPanel formSection = new JPanel(new BorderLayout(5, 8));
        formSection.setBackground(BG);
        formSection.add(form, BorderLayout.CENTER);
        formSection.add(actions, BorderLayout.SOUTH);

        content.add(formSection, BorderLayout.NORTH);

        // SEARCH PANEL
        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 8)
        );

        searchPanel.setBackground(BG);

        JLabel searchLabel = new JLabel("Search by Student ID:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));

        searchField = new JTextField(14);

        JButton searchButton = createButton("Search", BLUE);
        JButton showAllButton = createButton("Show All", NAVY);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // TABLE
        String[] columns = {
                "Student ID", "Name", "Course", "Email", "Marks"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(195, 215, 245));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createTitledBorder(
                "Students List"
        ));

        JPanel lower = new JPanel(new BorderLayout(8, 8));
        lower.setBackground(BG);
        lower.add(searchPanel, BorderLayout.NORTH);
        lower.add(scrollPane, BorderLayout.CENTER);

        content.add(lower, BorderLayout.CENTER);

        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);

        // FOOTER
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(220, 230, 245));
        footer.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        statusLabel = new JLabel("Database: Connecting...");
        totalLabel = new JLabel("Total Students: 0");

        footer.add(statusLabel, BorderLayout.WEST);
        footer.add(totalLabel, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);

        // BUTTON ACTIONS
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());

        searchButton.addActionListener(e -> searchStudent());
        showAllButton.addActionListener(e -> loadStudents());

        addMenu.addActionListener(e -> {
            clearFields();
            nameField.requestFocus();
        });

        viewMenu.addActionListener(e -> loadStudents());
        searchMenu.addActionListener(e -> searchField.requestFocus());
        updateMenu.addActionListener(e -> updateStudent());
        deleteMenu.addActionListener(e -> deleteStudent());

        importMenu.addActionListener(e -> importFromCSV());
        exportMenu.addActionListener(e -> exportToCSV());

        exitMenu.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // TABLE ROW SELECTION
        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = table.getSelectedRow();

                if (row != -1) {

                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    courseField.setText(model.getValueAt(row, 2).toString());
                    emailField.setText(model.getValueAt(row, 3).toString());
                    marksField.setText(model.getValueAt(row, 4).toString());
                }
            }
        });
    }

    // BUTTON DESIGN
    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        );

        return button;
    }

    // FORM ROW DESIGN
    private void addFormRow(
            JPanel panel, int row, String label, JTextField field) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(9, 12, 9, 12);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = row;

        JLabel text = new JLabel(label);
        text.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(text, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 32));

        panel.add(field, gbc);
    }

    // VALIDATE FORM
    private boolean validateFields() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String email = emailField.getText().trim();
        String marksText = marksField.getText().trim();

        if (id.isEmpty() || name.isEmpty() ||
                course.isEmpty() || email.isEmpty() ||
                marksText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "All fields are required!"
            );

            return false;
        }

        // Validate Student ID
        try {

            int studentId = Integer.parseInt(id);

            if (studentId <= 0) {

                JOptionPane.showMessageDialog(
                        this, "Student ID must be positive!"
                );

                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Student ID must contain numbers only!"
            );

            return false;
        }

        // Validate Name
        if (!name.matches("[a-zA-Z ]+")) {

            JOptionPane.showMessageDialog(
                    this, "Name should contain letters only!"
            );

            return false;
        }

        // Validate Email
        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid email address!"
            );

            return false;
        }

        // Validate Marks
        try {

            double marks = Double.parseDouble(marksText);

            if (marks < 0 || marks > 100) {

                JOptionPane.showMessageDialog(
                        this, "Marks must be between 0 and 100!"
                );

                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Marks must be a valid number!"
            );

            return false;
        }

        return true;
    }

    // ADD STUDENT
    private void addStudent() {

        if (!validateFields()) {
            return;
        }

        String sql = "INSERT INTO students "
                + "(student_id, name, course, email, marks) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idField.getText().trim()));
            ps.setString(2, nameField.getText().trim());
            ps.setString(3, courseField.getText().trim());
            ps.setString(4, emailField.getText().trim());
            ps.setDouble(5, Double.parseDouble(marksField.getText().trim()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this, "Student added successfully!"
            );

            clearFields();
            loadStudents();

        } catch (SQLIntegrityConstraintViolationException e) {

            JOptionPane.showMessageDialog(
                    this, "Student ID already exists!"
            );

        } catch (Exception e) {

            showError(e);
        }
    }

    // VIEW ALL STUDENTS
    private void loadStudents() {

        model.setRowCount(0);

        String sql = "SELECT * FROM students ORDER BY student_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("email"),
                        rs.getDouble("marks")
                });
            }

            statusLabel.setText("Database: MySQL Connected");
            totalLabel.setText(
                    "Total Students: " + model.getRowCount()
            );

        } catch (Exception e) {

            statusLabel.setText("Database: Connection Error");
            showError(e);
        }
    }

    // SEARCH STUDENT
    private void searchStudent() {

        String id = searchField.getText().trim();

        if (id.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Enter Student ID!"
            );

            return;
        }

        try {

            int studentId = Integer.parseInt(id);

            String sql = "SELECT * FROM students WHERE student_id = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, studentId);

                try (ResultSet rs = ps.executeQuery()) {

                    model.setRowCount(0);

                    if (rs.next()) {

                        model.addRow(new Object[]{
                                rs.getInt("student_id"),
                                rs.getString("name"),
                                rs.getString("course"),
                                rs.getString("email"),
                                rs.getDouble("marks")
                        });

                        JOptionPane.showMessageDialog(
                                this, "Student found!"
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                this, "Student not found!"
                        );
                    }

                    totalLabel.setText(
                            "Total Students: " + model.getRowCount()
                    );
                }
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid ID!"
            );

        } catch (Exception e) {

            showError(e);
        }
    }

    // UPDATE STUDENT
    private void updateStudent() {

        if (!validateFields()) {
            return;
        }

        String sql = "UPDATE students SET name=?, course=?, "
                + "email=?, marks=? WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nameField.getText().trim());
            ps.setString(2, courseField.getText().trim());
            ps.setString(3, emailField.getText().trim());
            ps.setDouble(4, Double.parseDouble(marksField.getText().trim()));
            ps.setInt(5, Integer.parseInt(idField.getText().trim()));

            int result = ps.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this, "Student updated!"
                );

                clearFields();
                loadStudents();

            } else {

                JOptionPane.showMessageDialog(
                        this, "Student ID not found!"
                );
            }

        } catch (Exception e) {

            showError(e);
        }
    }

    // DELETE STUDENT
    private void deleteStudent() {

        String id = idField.getText().trim();

        if (id.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Select a student or enter Student ID!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            int studentId = Integer.parseInt(id);

            String sql = "DELETE FROM students WHERE student_id=?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, studentId);

                int result = ps.executeUpdate();

                if (result > 0) {

                    JOptionPane.showMessageDialog(
                            this, "Student deleted successfully!"
                    );

                    clearFields();
                    loadStudents();

                } else {

                    JOptionPane.showMessageDialog(
                            this, "Student not found!"
                    );
                }
            }

        } catch (Exception e) {

            showError(e);
        }
    }

    // EXPORT STUDENTS TO CSV
    private void exportToCSV() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Student Records");
        chooser.setSelectedFile(new File("students.csv"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        String sql = "SELECT student_id, name, course, email, marks "
                + "FROM students ORDER BY student_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             BufferedWriter writer = new BufferedWriter(
                     new FileWriter(file))) {

            writer.write("student_id,name,course,email,marks");
            writer.newLine();

            int count = 0;

            while (rs.next()) {

                writer.write(
                        rs.getInt("student_id") + ","
                        + csvEscape(rs.getString("name")) + ","
                        + csvEscape(rs.getString("course")) + ","
                        + csvEscape(rs.getString("email")) + ","
                        + rs.getBigDecimal("marks")
                );

                writer.newLine();
                count++;
            }

            JOptionPane.showMessageDialog(
                    this,
                    count + " student record(s) exported successfully!\n"
                    + "File: " + file.getAbsolutePath()
            );

        } catch (Exception e) {

            showError(e);
        }
    }

    // IMPORT STUDENTS FROM CSV
    private void importFromCSV() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Student CSV File");

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        int imported = 0;
        int skipped = 0;
        int lineNumber = 0;

        String sql = "INSERT INTO students "
                + "(student_id, name, course, email, marks) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             BufferedReader reader = new BufferedReader(
                     new FileReader(file));
             PreparedStatement ps = con.prepareStatement(sql)) {

            String line;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                // Skip CSV header
                if (lineNumber == 1 &&
                        line.toLowerCase().contains("student_id")) {
                    continue;
                }

                try {

                    String[] values = parseCSVLine(line);

                    if (values.length != 5) {
                        skipped++;
                        continue;
                    }

                    int studentId = Integer.parseInt(values[0].trim());
                    String name = values[1].trim();
                    String course = values[2].trim();
                    String email = values[3].trim();
                    double marks = Double.parseDouble(values[4].trim());

                    // Validate imported data
                    if (studentId <= 0 ||
                            name.isEmpty() ||
                            course.isEmpty() ||
                            !name.matches("[a-zA-Z ]+") ||
                            !email.matches(
                                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                            || marks < 0 || marks > 100) {

                        skipped++;
                        continue;
                    }

                    ps.setInt(1, studentId);
                    ps.setString(2, name);
                    ps.setString(3, course);
                    ps.setString(4, email);
                    ps.setDouble(5, marks);

                    ps.executeUpdate();
                    imported++;

                } catch (SQLIntegrityConstraintViolationException e) {

                    // Duplicate Student ID
                    skipped++;

                } catch (IllegalArgumentException e) {

                    // Invalid CSV data
                    skipped++;
                }
            }

            loadStudents();

            JOptionPane.showMessageDialog(
                    this,
                    "CSV Import Completed!\n"
                    + "Imported: " + imported + "\n"
                    + "Skipped (invalid/duplicate): " + skipped
            );

        } catch (Exception e) {

            showError(e);
        }
    }

    // ESCAPE CSV VALUES
    private String csvEscape(String value) {

        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",") ||
                escaped.contains("\"") ||
                escaped.contains("\n") ||
                escaped.contains("\r")) {

            return "\"" + escaped + "\"";
        }

        return escaped;
    }

    // PARSE CSV ROW
    private String[] parseCSVLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char ch = line.charAt(i);

            if (ch == '"') {

                if (insideQuotes &&
                        i + 1 < line.length() &&
                        line.charAt(i + 1) == '"') {

                    value.append('"');
                    i++;

                } else {

                    insideQuotes = !insideQuotes;
                }

            } else if (ch == ',' && !insideQuotes) {

                values.add(value.toString());
                value.setLength(0);

            } else {

                value.append(ch);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException(
                    "Invalid CSV quotation marks."
            );
        }

        values.add(value.toString());

        return values.toArray(new String[0]);
    }

    // CLEAR FORM
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        emailField.setText("");
        marksField.setText("");
        searchField.setText("");

        table.clearSelection();
    }

    
    // ERROR HANDLING AND ERROR LOGGING
    private void showError(Exception e) {

        String errorType;
        String message;

        if (e instanceof SQLException) {

            errorType = "Database Error";
            message = "A database operation failed.";

        } else if (e instanceof IOException) {

            errorType = "File Error";
            message = "There was a problem reading or writing the file.";

        } else if (e instanceof IllegalArgumentException) {

            errorType = "Invalid Input";
            message = "Please check the entered data.";

        } else {

            errorType = "Application Error";
            message = "Something went wrong.";
        }

        // Save error details in log file
        try {

            Path logDirectory = Paths.get("logs");

            Files.createDirectories(logDirectory);

            Path logFile = logDirectory.resolve("application.log");

            String logMessage =
                    LocalDateTime.now()
                    + " | " + errorType
                    + " | " + e.toString()
                    + System.lineSeparator();

            Files.writeString(
                    logFile,
                    logMessage,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException logError) {

            System.out.println("Unable to write error log.");
            logError.printStackTrace();
        }

        // Display friendly error message
        JOptionPane.showMessageDialog(
                this,
                message + "\nDetails: " + e.getMessage()
                + "\n\nError has been recorded in logs/application.log",
                errorType,
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }


    // MAIN METHOD
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            StudentManagementGUI gui = new StudentManagementGUI();
            gui.setVisible(true);

        });
    }
}
