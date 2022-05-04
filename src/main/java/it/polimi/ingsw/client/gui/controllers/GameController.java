package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.customviews.*;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class GameController {
    @FXML
    public IslandCircularPane islandsPane;
    @FXML
    public CloudsCircularPane cloudsPane;
    @FXML
    public HBox player2Board;
    @FXML
    public Label player2BoardLabel;
    @FXML
    Button leaveButton;
    @FXML
    private Label player3YellowLabel;
    @FXML
    private Label player3RedLabel;
    @FXML
    private StudentView player2Yellow;
    @FXML
    private AssistantCardView player2Card;
    @FXML
    private HBox player2Coin;
    @FXML
    private Label player3GreenLabel;
    @FXML
    private Label player2TowerLabel;
    @FXML
    private Label player3PinkLabel;
    @FXML
    private StudentView player2Blue;
    @FXML
    private Label player2PinkLabel;
    @FXML
    private AssistantCardView player3Card;
    @FXML
    private Label myTowerLabel;
    @FXML
    private HBox player3Coin;
    @FXML
    private Label player3BlueLabel;
    @FXML
    private StudentView player3Green;
    @FXML
    private HBox myCoin;
    @FXML
    private AssistantCardView myCard;
    @FXML
    private Label player2CoinLabel;
    @FXML
    private Label player2RedLabel;
    @FXML
    private Label player2GreenLabel;
    @FXML
    private StudentView player3Blue;
    @FXML
    private StudentView player3Yellow;
    @FXML
    private StudentView player3Red;
    @FXML
    private Label player3CoinLabel;
    @FXML
    private Label player3TowerLabel;
    @FXML
    private TowerView myTower;
    @FXML
    private StudentView player2Pink;
    @FXML
    private TowerView player3Tower;
    @FXML
    private StudentView player3Pink;
    @FXML
    private Label infoLabel;
    @FXML
    private Label player2YellowLabel;
    @FXML
    private GridPane myStudentsBoard;
    @FXML
    private Label myCoinLabel;
    @FXML
    private Label player2BlueLabel;
    @FXML
    private TowerView player2Tower;
    @FXML
    private StudentView player2Red;
    @FXML
    private StudentView player2Green;

    @Deprecated
    public void initialize() {
        setVisibilityForNumberOfPlayers(3);
        setVisibilityForExpertMode(true);

        setMyStudentsBoard(
                new StudentsContainer()
                        .addStudents(Student.GREEN, 2)
                        .addStudents(Student.PINK, 1)
                        .addStudents(Student.RED, 3),
                new StudentsContainer()
                        .addStudents(Student.GREEN, 2)
                        .addStudents(Student.PINK, 1)
                        .addStudents(Student.BLUE, 1)
        );

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
                5,
                5,
                Tower.WHITE,
                false
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

        //setIslands();
        var c = new StudentsContainer()
                .addStudents(Student.BLUE, 1)
                .addStudents(Student.GREEN, 1)
                .addStudents(Student.PINK, 1);
        var l = Arrays.asList(c, c, c);
        setClouds(l);
    }

    @FXML
    private void onLeavePressed(){
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    public void setVisibilityForExpertMode(boolean expertMode) {
        player2Coin.setVisible(expertMode);
        player2Coin.setManaged(expertMode);

        player3Coin.setVisible(expertMode);
        player3Coin.setManaged(expertMode);

        myCoin.setVisible(expertMode);
        myCoin.setManaged(expertMode);
    }

    public void setVisibilityForNumberOfPlayers(int numberOfPlayers) {
        player2Board.setVisible(numberOfPlayers == 3);
        player2Board.setManaged(numberOfPlayers == 3);

        player2BoardLabel.setVisible(numberOfPlayers == 3);
        player2BoardLabel.setManaged(numberOfPlayers == 3);
    }

    public void setMyStudentsBoard(StudentsContainer entrance, StudentsContainer school) {
        myStudentsBoard.getChildren().clear();

        var l = new Label("Entrata");
        l.setId("my_students_board_small_label");
        myStudentsBoard.add(l, 2, 1);

        l = new Label("Sala");
        l.setId("my_students_board_small_label");
        myStudentsBoard.add(l, 2, 3);

        for(Student s : Student.values()) {
            var sv = new StudentView(s);
            sv.setFitWidth(40);
            sv.setFitHeight(40);
            myStudentsBoard.add(sv, s.ordinal(), 0);

            var entranceLabel = new Label(entrance.getCountForStudent(s) + "");
            entranceLabel.setId("my_students_board_label");
            myStudentsBoard.add(entranceLabel, s.ordinal(), 2);

            var schoolLabel = new Label(school.getCountForStudent(s) + "");
            schoolLabel.setId("my_students_board_label");
            myStudentsBoard.add(schoolLabel, s.ordinal(), 4);
        }

    }

    public void setClouds(List<StudentsContainer> clouds) {
        cloudsPane.getChildren().clear();
        clouds.forEach(c -> cloudsPane.addCloud(c));
    }
}
