package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class shows a student (or professor) of the specified color
 */
public class StudentView extends ImageView {

    private Student student;
    private boolean isProfessor;
    private static final Map<Student, Image> studentsImages = new EnumMap<>(Student.class);

    private static final Map<Student, Image> professorsImages = new EnumMap<>(Student.class);

    //load images statically
    static {
        for(Student s : Student.values()) {
            studentsImages.put(s, new Image(StudentView.class.getResourceAsStream("/assets/students/" + s.name().toLowerCase() + ".png")));
            professorsImages.put(s, new Image(StudentView.class.getResourceAsStream("/assets/professors/" + s.name().toLowerCase() + ".png")));
        }
    }

    /**
     * Create a StudentView as example
     */
    public StudentView() {
        this(Student.BLUE);
    }

    /**
     * Create a StudentView with isProfessor set to false
     * @param student student color
     */
    public StudentView(Student student) {
        this(student, false);
    }

    /**
     * Create a StudentView with specified student and professor status
     * @param student student color
     * @param isProfessor whether the student is a professor or not
     */
    public StudentView(Student student, boolean isProfessor) {
        super();
        this.student = student;
        this.isProfessor = isProfessor;

        setImage(getCurrentImage());
        setFitWidth(35);
        setPreserveRatio(true);
    }

    /**
     * Set whether the student is a professor or not
     * @param isProfessor
     */
    public void setProfessor(boolean isProfessor) {
        this.isProfessor = isProfessor;
        setImage(getCurrentImage());
    }

    /**
     * Set the student color to show
     * @param student
     */
    public void setStudent(Student student) {
        this.student = student;
        setImage(getCurrentImage());
    }

    /**
     * Get the student color
     * @return student color
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Get the current image to show (used internally)
     * @return
     */
    private Image getCurrentImage() {
        if(isProfessor)
            return professorsImages.get(student);
        else
            return studentsImages.get(student);
    }
}
