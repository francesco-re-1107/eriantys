package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;

import java.util.function.Consumer;

public class StudentSelectContextMenu extends ContextMenu {

    public StudentSelectContextMenu() {
        this(new StudentsContainer(), s -> {});
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
