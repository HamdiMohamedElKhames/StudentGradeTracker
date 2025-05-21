import java.util.*;

public class Student extends Person implements Gradable {
    private List<Double> grades;

    public Student(String name) {
        super(name);
        grades = new ArrayList<>();
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public List<Double> getGrades() {
        return grades;
    }

    @Override
    public double getAverage() {
        if (grades.isEmpty()) return 0;
        double total = 0;
        for (double g : grades) total += g;
        return total / grades.size();
    }
}
