import java.awt.*;
import java.util.*;
import javax.swing.*;

public class StudentGradeTrackerGUI {
    private JFrame frame;
    private JTextField nameField, gradeField, searchNameField;
    private JTextArea displayArea;
    private Map<String, Student> studentMap = new HashMap<>();

    public StudentGradeTrackerGUI() {
        frame = new JFrame("Student Grade Tracker");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        nameField = new JTextField(20);
        gradeField = new JTextField(5);
        searchNameField = new JTextField(20);

        displayArea = new JTextArea(15, 35);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        frame.add(new JLabel("Student name:"));
        frame.add(nameField);
        frame.add(new JLabel("Grade:"));
        frame.add(gradeField);

        JButton addButton = new JButton("Add Grade");
        JButton viewButton = new JButton("See All");
        JButton avgAllButton = new JButton("General Grade");
        JButton avgOneButton = new JButton("Grade per Student");

        frame.add(addButton);
        frame.add(viewButton);
        frame.add(avgAllButton);
        frame.add(new JLabel("Search for a student:"));
        frame.add(searchNameField);
        frame.add(avgOneButton);
        frame.add(scrollPane);

        addButton.addActionListener(e -> handleAddGrade());
        viewButton.addActionListener(e -> displayAllStudents());
        avgAllButton.addActionListener(e -> calculateGlobalAverage());
        avgOneButton.addActionListener(e -> calculateStudentAverage());

        frame.setVisible(true);
    }

    private void handleAddGrade() {
        String name = nameField.getText().trim();
        String gradeText = gradeField.getText().trim();

        try {
            if (!name.matches("[a-zA-Z ]+")) {
                throw new IllegalArgumentException("Invalid student name.");
            }

            double grade = parseGrade(gradeText);

            studentMap.putIfAbsent(name, new Student(name));
            studentMap.get(name).addGrade(grade);

            displayArea.append("Added: " + name + " -> " + grade + "\n");

            nameField.setText("");
            gradeField.setText("");

        } catch (InvalidGradeException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }

    private double parseGrade(String gradeText) throws InvalidGradeException {
        try {
            double grade = Double.parseDouble(gradeText);
            if (grade < 0 || grade > 20) {
                throw new InvalidGradeException("Grade must be between 0 and 20.");
            }
            return grade;
        } catch (NumberFormatException e) {
            throw new InvalidGradeException("Invalid grade format.");
        }
    }

    private void displayAllStudents() {
        displayArea.setText("");
        if (studentMap.isEmpty()) {
            displayArea.setText("No students recorded.");
            return;
        }
        for (Student student : studentMap.values()) {
            displayArea.append(student.getName() + ": " + student.getGrades() + "\n");
        }
    }

    private void calculateGlobalAverage() {
        double total = 0;
        int count = 0;

        for (Student student : studentMap.values()) {
            for (double grade : student.getGrades()) {
                total += grade;
                count++;
            }
        }

        if (count == 0) {
            displayArea.setText("No grades recorded.");
            return;
        }

        double avg = total / count;
        displayArea.setText("General average: " + String.format("%.2f", avg));
    }

    private void calculateStudentAverage() {
        String name = searchNameField.getText().trim();

        Student student = studentMap.get(name);
        if (student == null) {
            displayArea.setText("Student not found.");
            return;
        }

        double avg = student.getAverage();
        if (student.getGrades().isEmpty()) {
            displayArea.setText(name + " has no grades.");
        } else {
            displayArea.setText("Average for " + name + ": " + String.format("%.2f", avg));
        }
    }

    public static void main(String[] args) {
        new StudentGradeTrackerGUI();
    }
}
