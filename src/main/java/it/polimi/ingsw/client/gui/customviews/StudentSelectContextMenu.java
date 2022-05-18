package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;

import java.util.function.Consumer;

/**
 * This class shows a context menu for the student selection
 */
public class StudentSelectContextMenu extends ContextMenu {

    /**
     * Context menu with all students and no listener
     */
    public StudentSelectContextMenu() {
        this(s -> {});
    }

    /**
     * Context menu with all students and custom listener
     * @param onStudentSelected
     */
    public StudentSelectContextMenu(Consumer<Student> onStudentSelected) {
        this(
                new StudentsContainer()
                        .addStudent(Student.YELLOW)
                        .addStudent(Student.RED)
                        .addStudent(Student.BLUE)
                        .addStudent(Student.GREEN)
                        .addStudent(Student.PINK),
                onStudentSelected
        );
    }

    public StudentSelectContextMenu(StudentsContainer studentsSelectable, Consumer<Student> onStudentSelected) {
        super();

        for(Student s : Student.values()) {
            if(studentsSelectable.getCountForStudent(s) <= 0)
                continue;

            var sv = new StudentView(s);
            sv.setFitWidth(30);
            sv.setFitHeight(30);

            var mi = new CustomMenuItem(sv, true);

            getItems().add(mi);
            mi.setOnAction(event -> onStudentSelected.accept(s));
        }
    }

}
