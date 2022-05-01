package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class StudentView extends ImageView {

    private Student student;
    private boolean isProfessor;

    public StudentView() {
        this(Student.BLUE);
    }

    public StudentView(Student student) {
        this(student, false);
        setFitWidth(35);
        setFitHeight(35);
    }

    public StudentView(Student student, boolean isProfessor) {
        super();
        this.student = student;
        this.isProfessor = isProfessor;

        setImage(new Image(getImageStream()));
    }

    public void setProfessor(boolean isProfessor) {
        this.isProfessor = isProfessor;
        setImage(new Image(getImageStream()));
    }

    public void setStudent(Student student) {
        this.student = student;
        setImage(new Image(getImageStream()));
    }

    public boolean isProfessor() {
        return isProfessor;
    }

    public Student getStudent() {
        return student;
    }

    private InputStream getImageStream() {
        if(isProfessor)
            return getClass().getResourceAsStream("/assets/professors/" + student.name().toLowerCase() + ".png");
        else
            return getClass().getResourceAsStream("/assets/students/" + student.name().toLowerCase() + ".png");
    }
}
