package it.polimi.ingsw.client.gui.customviews;

import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.server.model.Student;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class StudentsBoardView extends GridPane {

    private Map<Student, Label> entranceLabels = new EnumMap<>(Student.class);

    private Map<Student, Label> schoolLabels = new EnumMap<>(Student.class);

    private Map<Student, StudentView> studentViews = new EnumMap<>(Student.class);
    private Consumer<Student> listener;
    private ReducedPlayer player;

    public StudentsBoardView() {
        super();

        setId("students_board");
        setAlignment(Pos.CENTER);
        setHgap(5);
        setVgap(5);
        setupConstraints();
        setupElements();

        setStudentsClickDisable(true);

        getStylesheets().add(getClass().getResource("/css/students_board_view.css").toExternalForm());
    }

    private void setupConstraints() {
        var c = new ColumnConstraints();
        c.setHgrow(Priority.ALWAYS);
        c.setHalignment(HPos.CENTER);

        var r = new RowConstraints();
        r.setVgrow(Priority.SOMETIMES);

        for (int i = 0; i < Student.values().length; i++) {
            getColumnConstraints().add(i, c);
            getRowConstraints().add(i, r);
        }
    }

    private void setupElements() {
        var entranceLabel = new Label("Entrata");
        entranceLabel.setId("small_label");
        add(entranceLabel, 2, 1);

        var schoolLabel = new Label("Sala");
        schoolLabel.setId("small_label");
        add(schoolLabel, 2, 3);

        for (Student s : Student.values()) {
            var sv = new StudentView(s);
            studentViews.put(s, sv);
            sv.setFitWidth(40);
            sv.setId("student_view");
            sv.setOnMouseClicked(e -> listener.accept(s));
            add(sv, s.ordinal(), 0);

            var el = new Label();
            entranceLabels.put(s, el);
            el.setId("big_label");
            add(el, s.ordinal(), 2);

            var sl = new Label();
            schoolLabels.put(s, sl);
            sl.setId("big_label");
            add(sl, s.ordinal(), 4);
        }
    }

    public void setOnStudentClickListener(Consumer<Student> listener) {
        this.listener = listener;
    }

    public void setPlayer(ReducedPlayer player) {
        this.player = player;

        for(Student s : Student.values()) {
            entranceLabels.get(s).setText(String.valueOf(player.entrance().getCountForStudent(s)));
            schoolLabels.get(s).setText(String.valueOf(player.school().getCountForStudent(s)));
        }
    }

    public void setProfessors(Map<Student, String> professors) {
        for(Student s : Student.values()) {
            var hasProfessor = Objects.equals(player.nickname(), professors.get(s));
            studentViews.get(s).setProfessor(hasProfessor);
        }
    }

    public void setStudentsClickDisable(boolean disable) {
        for(Student s : Student.values()) {
            studentViews.get(s).setDisable(disable);
        }
    }
}
