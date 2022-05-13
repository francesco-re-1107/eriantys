package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class StudentView extends ImageView {

    private Student student;
    private boolean isProfessor;
    private static final Map<Student, Image> studentsImages = new HashMap<>();

    private static final Map<Student, Image> professorsImages = new HashMap<>();

    static {
        for(Student s : Student.values()) {
            studentsImages.put(s, new Image(StudentView.class.getResourceAsStream("/assets/students/" + s.name().toLowerCase() + ".png")));
            professorsImages.put(s, new Image(StudentView.class.getResourceAsStream("/assets/professors/" + s.name().toLowerCase() + ".png")));
        }
    }

    public StudentView() {
        this(Student.BLUE);
    }

    public StudentView(Student student) {
        this(student, false);
    }

    public StudentView(Student student, boolean isProfessor) {
        super();
        this.student = student;
        this.isProfessor = isProfessor;

        setImage(getCurrentImage());
        setFitWidth(35);
        setFitHeight(35);
        setPreserveRatio(true);
    }

    public void setProfessor(boolean isProfessor) {
        this.isProfessor = isProfessor;
        setImage(getCurrentImage());
    }

    public void setStudent(Student student) {
        this.student = student;
        setImage(getCurrentImage());
    }

    public boolean isProfessor() {
        return isProfessor;
    }

    public Student getStudent() {
        return student;
    }

    private Image getCurrentImage() {
        if(isProfessor)
            return professorsImages.get(student);
        else
            return studentsImages.get(student);
    }
}
