package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.EnumMap;
import java.util.Map;

public class StudentView extends ImageView {

    private Student student;
    private boolean isProfessor;
    private static final Map<Student, Image> studentsImages = new EnumMap<>(Student.class);

    private static final Map<Student, Image> professorsImages = new EnumMap<>(Student.class);

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

    public void setSelected(boolean selected) {
        if(selected) {
            setEffect(new Glow(0.2));
            setScaleX(1.3);
            setScaleY(1.3);
        } else {
            setEffect(null);
            setScaleX(1.0);
            setScaleY(1.0);
        }
    }
}
