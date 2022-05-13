package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.gui.InfoStrings;
import it.polimi.ingsw.client.gui.customviews.*;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.common.requests.PlayAssistantCardRequest;
import it.polimi.ingsw.server.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GameController implements ScreenController, Client.GameUpdateListener {
    @FXML
    public IslandCircularPane islandsPane;
    @FXML
    public CloudsCircularPane cloudsPane;
    @FXML
    public HBox player3Board;
    @FXML
    public Label player2NicknameLabel;
    @FXML
    public Label player3NicknameLabel;
    @FXML
    public VBox player2Students;
    @FXML
    public VBox player3Students;
    @FXML
    public VBox characterCardsView;
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
    private ReducedGame lastGame;

    @FXML
    public void initialize() {
        //...
    }

    private void setAssistantDeckVisible(boolean visible) {
        assistantCardsLayer.setVisible(visible);
        assistantCardsLayer.setManaged(visible);
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
            acv.setOnMouseClicked(e -> {
                Client.getInstance().forwardGameRequest(
                        new PlayAssistantCardRequest(c),
                        () -> setAssistantDeckVisible(false),
                        err -> Utils.LOGGER.info("Error playing assistant card " + err.getMessage())
                );
            });
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
        Client.getInstance().leaveGame(e -> {
            //show error...
            Utils.LOGGER.info("Error leaving game: " + e.getMessage());
        });
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

        characterCardsView.setVisible(expertMode);
        characterCardsView.setManaged(expertMode);
    }

    public void setVisibilityForNumberOfPlayers(int numberOfPlayers) {
        player3Board.setVisible(numberOfPlayers == 3);
        player3Board.setManaged(numberOfPlayers == 3);

        player3NicknameLabel.setVisible(numberOfPlayers == 3);
        player3NicknameLabel.setManaged(numberOfPlayers == 3);
    }

    public void setMyStudentsBoard(ReducedPlayer myPlayer, Map<Student, ReducedPlayer> professors) {
        myStudentsBoard.getChildren().clear();

        var l = new Label("Entrata");
        l.setId("my_students_board_small_label");
        myStudentsBoard.add(l, 2, 1);

        l = new Label("Sala");
        l.setId("my_students_board_small_label");
        myStudentsBoard.add(l, 2, 3);

        for(Student s : Student.values()) {
            var sv = new StudentView(s, myPlayer.equals(professors.get(s)));
            sv.setFitWidth(40);
            sv.setFitHeight(40);
            myStudentsBoard.add(sv, s.ordinal(), 0);

            var entranceLabel = new Label(myPlayer.entrance().getCountForStudent(s) + "");
            entranceLabel.setId("my_students_board_label");
            myStudentsBoard.add(entranceLabel, s.ordinal(), 2);

            var schoolLabel = new Label(myPlayer.school().getCountForStudent(s) + "");
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

    public void setPlayer2Board(ReducedPlayer player2, Map<Student, ReducedPlayer> professors) {
        setPlayerBoard(player2, professors, player2NicknameLabel, player2Students, player2CoinLabel, player2TowerLabel, player2Tower);
    }

    public void setPlayer3Board(ReducedPlayer player3, Map<Student, ReducedPlayer> professors) {
        setPlayerBoard(player3, professors, player3NicknameLabel, player3Students, player3CoinLabel, player3TowerLabel, player3Tower);
    }

    private void setPlayerBoard(ReducedPlayer player, Map<Student, ReducedPlayer> professors, Label nicknameLabel, VBox playerStudents, Label playerCoinLabel, Label playerTowerLabel, TowerView playerTower) {
        playerStudents.getChildren().clear();

        nicknameLabel.setText(player.nickname());

        for(Student s : Student.values()) {
            var hbox = new HBox();
            hbox.setSpacing(5);
            var sv = new StudentView(s, player.equals(professors.get(s)));
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

    @Override
    public void onCreate() {

    }

    @Override
    public void onShow() {
        Client.getInstance().addGameUpdateListener(this);
    }

    @Override
    public void onHide() {
        Client.getInstance().removeGameUpdateListener(this);
    }

    @Override
    public void onGameUpdate(ReducedGame game) {
        Platform.runLater(() -> gameUpdate(game));
    }

    private void gameUpdate(ReducedGame game) {
        lastGame = game;
        var myPlayer = findMyPlayer(game);
        var otherPlayers = new ArrayList<>(game.players());
        otherPlayers.remove(myPlayer);
        otherPlayers.sort(Comparator.comparing(ReducedPlayer::nickname));

        setVisibilityForNumberOfPlayers(game.numberOfPlayers());
        setVisibilityForExpertMode(game.expertMode());
        setIslands(game.islands());
        setMotherNatureIndex(game.motherNaturePosition());
        setClouds(game.currentRound().clouds());
        setMyBoard(myPlayer, game.currentProfessors());
        setPlayer2Board(otherPlayers.get(0), game.currentProfessors());
        if(game.numberOfPlayers() > 2)
            setPlayer3Board(otherPlayers.get(1), game.currentProfessors());

        /*switch(game.currentState()) {
            case CREATED ->
            case STARTED ->
            case PAUSED ->
            case FINISHED ->
            case TERMINATED ->
        }*/

        var currentPlayer = game.currentRound().currentPlayer();

        //my turn
        if (currentPlayer.equals(myPlayer)){
            if(game.currentRound().stage() instanceof Stage.Attack) { //attack

            } else { //plan
                setAssistantDeckVisible(true);
            }
        }else{
            setInfoString(InfoStrings.OTHER_PLAYER_WAIT_FOR_HIS_TURN, currentPlayer.nickname());
        }
    }

    private void setMyBoard(ReducedPlayer myPlayer, Map<Student, ReducedPlayer> professors) {
        setMyStudentsBoard(myPlayer, professors);
        setMyCoin(myPlayer.coins());
        setMyTowers(myPlayer.towerColor(), myPlayer.towersCount());
        setAssistantCardsDeck(myPlayer.deck());
    }

    private ReducedPlayer findMyPlayer(ReducedGame game) {
        return game.players()
                .stream()
                .filter(p -> p.nickname().equals(Client.getInstance().getNickname()))
                .findFirst()
                .orElse(null);
    }
}
