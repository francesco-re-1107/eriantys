package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.InfoStrings;
import it.polimi.ingsw.client.gui.NavigationManager;
import it.polimi.ingsw.client.gui.customviews.*;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    @FXML
    public IslandCircularPane islandsPane;
    @FXML
    public CloudsCircularPane cloudsPane;
    @FXML
    public HBox player3Board;
    @FXML
    public Label player3BoardLabel;
    @FXML
    public VBox player2Students;
    @FXML
    public VBox player3Students;
    @FXML
    public HBox characterCards;
    @FXML
    public CharacterCardView characterCard1;
    @FXML
    public CharacterCardView characterCard2;
    @FXML
    public CharacterCardView characterCard3;
    @FXML
    public VBox assistantCardsDeck;
    @FXML
    public VBox assistantCardsLayer;
    @FXML
    Button leaveButton;
    @FXML
    private AssistantCardView player2Card;
    @FXML
    private HBox player2Coin;
    @FXML
    private Label player2TowerLabel;
    @FXML
    private AssistantCardView player3Card;
    @FXML
    private Label myTowerLabel;
    @FXML
    private HBox player3Coin;
    @FXML
    private HBox myCoin;
    @FXML
    private AssistantCardView myCard;
    @FXML
    private Label player2CoinLabel;
    @FXML
    private Label player3CoinLabel;
    @FXML
    private Label player3TowerLabel;
    @FXML
    private TowerView myTower;
    @FXML
    private TowerView player3Tower;
    @FXML
    private Label infoLabel;
    @FXML
    private GridPane myStudentsBoard;
    @FXML
    private Label myCoinLabel;
    @FXML
    private TowerView player2Tower;

    @FXML
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

        var islands = Arrays.asList(i, i, i, i, i, i, i, i, i, i, i, i);

        setIslands(islands);

        characterCards.setDisable(true);

        setMotherNatureIndex(4);
        setMotherNaturePossibleSteps(4, 3);

        setInfoString(InfoStrings.OTHER_PLAYER_WAIT_FOR_HIS_TURN, "player2");

        var c = new StudentsContainer()
                .addStudents(Student.BLUE, 1)
                .addStudents(Student.GREEN, 1)
                .addStudents(Student.PINK, 1);
        var l = Arrays.asList(c, c, c);
        setClouds(l);

        setMyCoin(0);
        setMyTowers(Tower.WHITE, 7);

        var deck = new HashMap<AssistantCard, Boolean>();
        for(var card : AssistantCard.getDefaultDeck()) {
            deck.put(card, false);
        }
        deck.put(AssistantCard.getDefaultDeck().get(0), true);

        setAssistantCardsDeck(deck);
        assistantCardsLayer.setVisible(false);

        setPlayer2Board(new ReducedPlayer(
                "p2",
                true,
                new StudentsContainer()
                        .addStudents(Student.BLUE, 1)
                        .addStudents(Student.GREEN, 1)
                        .addStudents(Student.PINK, 1),
                new StudentsContainer()
                        .addStudents(Student.BLUE, 1)
                        .addStudents(Student.GREEN, 1)
                        .addStudents(Student.PINK, 1),
                6,
                Tower.BLACK,
                null,
                0
        ));

        setPlayer3Board(new ReducedPlayer(
                "p3",
                true,
                new StudentsContainer()
                        .addStudents(Student.BLUE, 1)
                        .addStudents(Student.GREEN, 1)
                        .addStudents(Student.PINK, 1),
                new StudentsContainer()
                        .addStudents(Student.BLUE, 1)
                        .addStudents(Student.GREEN, 1)
                        .addStudents(Student.PINK, 1),
                7,
                Tower.GREY,
                null,
                0
        ));
    }

    private void setAssistantCardsDeck(Map<AssistantCard, Boolean> deck) {
        assistantCardsDeck.getChildren().clear();
        assistantCardsDeck.getChildren().add(new Label("Seleziona una carta"));

        var hbox = new HBox();
        hbox.setSpacing(30);
        hbox.setAlignment(Pos.CENTER);

        assistantCardsDeck.getChildren().add(hbox);

        for(var c : AssistantCard.getDefaultDeck()) {
            var acv = new AssistantCardView();
            acv.setCard(c);
            acv.setGrayedOut(deck.get(c));
            acv.setDisable(deck.get(c));
            acv.setMaxWidth(120);

            hbox.getChildren().add(acv);

            if (AssistantCard.getDefaultDeck().indexOf(c) == 4) {
                hbox = new HBox();
                hbox.setSpacing(30);
                hbox.setAlignment(Pos.CENTER);

                assistantCardsDeck.getChildren().add(hbox);
            }
        }

    }

    private void setInfoString(String info, String... optionalArgs) {
        if(InfoStrings.EMPTY.equals(info)) {
            infoLabel.setVisible(false);
            infoLabel.setManaged(false);

        }else {
            infoLabel.setVisible(true);
            infoLabel.setManaged(true);
        }
        infoLabel.setText(String.format(info, optionalArgs));
    }

    private void setMyTowers(Tower towerColor, int numberOfTowers) {
        myTower.setTowerColor(towerColor);
        myTowerLabel.setText(numberOfTowers + "");
    }

    @FXML
    private void onLeavePressed(){
        NavigationManager.getInstance().clearBackStack();
        NavigationManager.getInstance().navigateTo(NavigationManager.Screen.MAIN_MENU, false);
    }

    public void setMotherNatureIndex(int index){
        //reset
        for (var iv : islandsPane.getChildren())
            ((IslandView) iv).getMotherNatureView().setState(MotherNatureView.State.INVISIBLE);

        var iv = (IslandView) islandsPane.getChildren().get(index);
        iv.getMotherNatureView().setState(MotherNatureView.State.ENABLED);
    }

    public void setMotherNaturePossibleSteps(int index, int steps){
        //reset previous possible steps
        for (var iv : islandsPane.getChildren()) {
            var mn = ((IslandView) iv).getMotherNatureView();
            iv.setDisable(true);

            if(mn.getState() != MotherNatureView.State.ENABLED)
                mn.setState(MotherNatureView.State.INVISIBLE);
        }

        var currIndex = (index + 1) % islandsPane.getChildren().size();;

        while (steps > 0){
            var iv = (IslandView) islandsPane.getChildren().get(currIndex);
            if(currIndex != index)
                iv.getMotherNatureView().setState(MotherNatureView.State.DISABLED);
            iv.setDisable(false);
            currIndex = (currIndex + 1) % islandsPane.getChildren().size();
            steps--;
        }
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
        player3Board.setVisible(numberOfPlayers == 3);
        player3Board.setManaged(numberOfPlayers == 3);

        player3BoardLabel.setVisible(numberOfPlayers == 3);
        player3BoardLabel.setManaged(numberOfPlayers == 3);
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

    public void setIslands(List<ReducedIsland> islands) {
        islandsPane.getChildren().clear();
        islands.forEach(i -> islandsPane.addIsland(i));
    }

    public void setMyCoin(int coin) {
        myCoinLabel.setText(coin + "");
    }

    public void setPlayer2Board(ReducedPlayer player2) {
        setPlayerBoard(player2, player2Students, player2CoinLabel, player2TowerLabel, player2Tower);
    }

    public void setPlayer3Board(ReducedPlayer player3) {
        setPlayerBoard(player3, player3Students, player3CoinLabel, player3TowerLabel, player3Tower);
    }

    private void setPlayerBoard(ReducedPlayer player, VBox playerStudents, Label playerCoinLabel, Label playerTowerLabel, TowerView playerTower) {
        playerStudents.getChildren().clear();

        for(Student s : Student.values()) {
            var hbox = new HBox();
            hbox.setSpacing(5);
            var sv = new StudentView(s);
            sv.setFitWidth(25);
            sv.setFitHeight(25);

            var label = new Label(player.school().getCountForStudent(s) + "(" + player.entrance().getCountForStudent(s) + ")");

            hbox.getChildren().addAll(sv, label);

            playerStudents.getChildren().add(hbox);
        }

        playerCoinLabel.setText(player.coins() + "");
        playerTowerLabel.setText(player.towersCount() + "");
        playerTower.setTowerColor(player.towerColor());
    }
}
