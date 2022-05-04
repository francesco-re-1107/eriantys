package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.customviews.CloudsCircularPane;
import it.polimi.ingsw.client.gui.customviews.IslandCircularPane;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameController {
    @FXML
    public IslandCircularPane islandsPane;
    @FXML
    public CloudsCircularPane cloudsPane;
    @FXML
    Button leaveButton;

    @FXML
    public void initialize() {
        var i = new ReducedIsland(
                new StudentsContainer()
                        .addStudents(Student.BLUE, 1)
                        .addStudents(Student.RED, 1)
                        .addStudents(Student.PINK, 1),
                1,
                0,
                Tower.GREY,
                false
        );

        var i2 = new ReducedIsland(
                new StudentsContainer()
                        .addStudents(Student.RED, 1)
                        .addStudents(Student.YELLOW, 4),
                2,
                2,
                Tower.WHITE,
                true
        );

        var i3 = new ReducedIsland(
                new StudentsContainer()
                        .addStudents(Student.YELLOW, 2)
                        .addStudents(Student.RED, 1)
                        .addStudents(Student.GREEN, 1),
                8,
                8,
                Tower.GREY,
                false
        );

        islandsPane.addIsland(i);
        islandsPane.addIsland(i);
        islandsPane.addIsland(i);
        islandsPane.addIsland(i2);
        islandsPane.addIsland(i3);
    }

    @FXML
    private void onLeavePressed(){
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

}
