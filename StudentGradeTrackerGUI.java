import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class StudentGradeTrackerGUI {
    private JFrame frame;
    private JTextField nameField, gradeField, searchNameField;
    private JTextArea displayArea;
    private Map<String, java.util.List<Double>> studentMap = new HashMap<>();

    public StudentGradeTrackerGUI() {
        frame = new JFrame("Student Grade Tracker");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("student name:");
        nameField = new JTextField(20);
        JLabel gradeLabel = new JLabel("grade:");
        gradeField = new JTextField(5);

        JButton addButton = new JButton("add grade");
        JButton viewButton = new JButton("see all");
        JButton avgAllButton = new JButton("general grade");
        JButton avgOneButton = new JButton("grade per student");

        JLabel searchLabel = new JLabel("search for grade of only one existant student:");
        searchNameField = new JTextField(20);

        displayArea = new JTextArea(15, 35);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(gradeLabel);
        frame.add(gradeField);
        frame.add(addButton);
        frame.add(viewButton);
        frame.add(avgAllButton);
        frame.add(searchLabel);
        frame.add(searchNameField);
        frame.add(avgOneButton);
        frame.add(scrollPane);

        addButton.addActionListener(e -> addGrade());
        viewButton.addActionListener(e -> displayAllStudents());
        avgAllButton.addActionListener(e -> calculateGlobalAverage());
        avgOneButton.addActionListener(e -> calculateStudentAverage());

        frame.setVisible(true);
    }

    private void addGrade() {
        String name = nameField.getText().trim();
        String gradeText = gradeField.getText().trim();

        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(frame, "name entered is not valid.");
            return;
        }

        try {
            double grade = Double.parseDouble(gradeText);
            if (grade < 0 || grade > 20) {
                JOptionPane.showMessageDialog(frame, "grade should be between 0 and 20.");
                return;
            }

            studentMap.putIfAbsent(name, new ArrayList<>());
            studentMap.get(name).add(grade);

            displayArea.append("student: " + name + " grade: " + grade + "\n");
            nameField.setText("");
            gradeField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "please enter a valid grade.");
        }
    }

    private void displayAllStudents() {
        displayArea.setText("");
        if (studentMap.isEmpty()) {
            displayArea.setText("list is empty.");
            return;
        }

        for (String name : studentMap.keySet()) {
            displayArea.append(name + " : " + studentMap.get(name) + "\n");
        }
    }

    private void calculateGlobalAverage() {
        double total = 0;
        int count = 0;

        for (List<Double> grades : studentMap.values()) {
            for (double g : grades) {
                total += g;
                count++;
            }
        }

        if (count == 0) {
            displayArea.setText("empty.");
            return;
        }

        double avg = total / count;
        displayArea.setText("general grade : " + String.format("%.2f", avg));
    }

    private void calculateStudentAverage() {
        String name = searchNameField.getText().trim();
        if (!studentMap.containsKey(name)) {
            displayArea.setText("can't find student please try again.");
            return;
        }

        List<Double> grades = studentMap.get(name);
        if (grades.isEmpty()) {
            displayArea.setText("this student don't have any grade recorded.");
            return;
        }

        double total = 0;
        for (double g : grades) total += g;
        double avg = total / grades.size();

        displayArea.setText("grade of " + name +' '+ String.format("%.2f", avg));
    }

    public static void main(String[] args) {
        new StudentGradeTrackerGUI();
    }
}
