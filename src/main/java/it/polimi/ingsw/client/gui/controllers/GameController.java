package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.gui.InfoStrings;
import it.polimi.ingsw.client.gui.customviews.*;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.common.requests.MoveMotherNatureRequest;
import it.polimi.ingsw.common.requests.PlaceStudentsRequest;
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

import java.util.*;
import java.util.stream.Collectors;

public class GameController implements ScreenController, Client.GameUpdateListener {
    @FXML
    public IslandCircularPane islandsPane;
    @FXML
    public CloudsPane cloudsPane;
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
    public Button hideAssistantCardsDeck;
    @FXML
    public GameTitlePopupView gameTitlePopup;
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
    private ReducedGame currentGame;

    private StudentsContainer studentsPlacedInSchool;

    private Map<Integer, StudentsContainer> studentsPlacedInIslands;
    private String myNickname;

    private StudentSelectContextMenu studentSelectContextMenu;

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

        for (var c : AssistantCard.getDefaultDeck()) {
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

    private void setInfoString(String info, Object... optionalArgs) {
        if (InfoStrings.EMPTY.equals(info)) {
            infoLabel.setVisible(false);
            infoLabel.setManaged(false);

        } else {
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
    private void onLeavePressed() {
        Client.getInstance().leaveGame();
    }

    public void setMotherNatureIndex(int index) {
        //reset
        for (var iv : islandsPane.getChildren())
            ((IslandView) iv).getMotherNatureView().setState(MotherNatureView.State.INVISIBLE);

        var iv = (IslandView) islandsPane.getChildren().get(index);
        iv.getMotherNatureView().setState(MotherNatureView.State.ENABLED);
    }

    public void setMotherNaturePossibleSteps(int index, int steps) {
        //reset previous possible steps
        for (var n : islandsPane.getChildren()) {
            var iv = (IslandView) n;
            var mn = iv.getMotherNatureView();
            iv.setDisable(true);

            if (mn.getState() != MotherNatureView.State.ENABLED)
                mn.setState(MotherNatureView.State.INVISIBLE);
        }

        var currIndex = (index + 1) % islandsPane.getChildren().size();

        var currStep = 1;

        while (currStep <= steps) {
            //TODO: improve
            final var currStepFinal = currStep;

            var iv = (IslandView) islandsPane.getChildren().get(currIndex);
            if (currIndex != index) {
                iv.getMotherNatureView().setState(MotherNatureView.State.DISABLED);
                iv.setOnMouseClicked(e -> {
                    Client.getInstance().forwardGameRequest(
                            new MoveMotherNatureRequest(currStepFinal),
                            () -> {},
                            err -> Utils.LOGGER.info("Error moving mother nature: " + err.getMessage())
                    );
                });
            }
            iv.setDisable(false);
            currIndex = (currIndex + 1) % islandsPane.getChildren().size();
            currStep++;
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

    public void setMyStudentsBoard(ReducedPlayer myPlayer, Map<Student, String> professors) {
        myStudentsBoard.getChildren().clear();

        var l = new Label("Entrata");
        l.setId("my_students_board_small_label");
        myStudentsBoard.add(l, 2, 1);

        l = new Label("Sala");
        l.setId("my_students_board_small_label");
        myStudentsBoard.add(l, 2, 3);

        for (Student s : Student.values()) {
            var sv = new StudentView(s, myNickname.equals(professors.get(s)));
            sv.setFitWidth(40);
            sv.setFitHeight(40);
            sv.setId("selectable_student_view");
            sv.setOnMouseClicked(e -> placeStudentInSchool(s));
            myStudentsBoard.add(sv, s.ordinal(), 0);

            var entranceLabel = new Label(myPlayer.entrance().getCountForStudent(s) + "");
            entranceLabel.setId("my_students_board_label");
            myStudentsBoard.add(entranceLabel, s.ordinal(), 2);

            var schoolLabel = new Label(myPlayer.school().getCountForStudent(s) + "");
            schoolLabel.setId("my_students_board_label");
            myStudentsBoard.add(schoolLabel, s.ordinal(), 4);
        }
    }

    private boolean checkIfAllStudentsPlaced(){
        var count = 0;
        count += studentsPlacedInSchool.getSize();
        for(StudentsContainer sc : studentsPlacedInIslands.values())
            count += sc.getSize();

        var studentsToMove = currentGame.numberOfPlayers() == 2 ?
                Constants.TwoPlayers.STUDENTS_TO_MOVE : Constants.ThreePlayers.STUDENTS_TO_MOVE;

        if(count == studentsToMove) {
            Client.getInstance()
                    .forwardGameRequest(
                            new PlaceStudentsRequest(
                                    studentsPlacedInSchool,
                                    studentsPlacedInIslands
                            ),
                            () -> {
                            },
                            err -> Utils.LOGGER.info("Error placing students: " + err)
                    );
            return true;
        } else {
            setInfoString(InfoStrings.MY_TURN_PLACE_STUDENTS, studentsToMove-count);
            setMyStudentsBoard(currentGame.currentRound().currentPlayer(), currentGame.currentProfessors());
        }
        return false;
    }

    private void placeStudentInSchool(Student s) {
        var myPlayer = currentGame.currentRound().currentPlayer();

        if(myPlayer.entrance().getCountForStudent(s) <= 0) return;

        myPlayer.entrance().removeStudent(s);
        myPlayer.school().addStudent(s);

        studentsPlacedInSchool.addStudent(s);

        checkIfAllStudentsPlaced();
    }

    private void placeStudentInIsland(Student s, IslandView islandView) {
        var myPlayer = currentGame.currentRound().currentPlayer();

        if(myPlayer.entrance().getCountForStudent(s) <= 0) return;

        myPlayer.entrance().removeStudent(s);

        //store student in island
        var index = islandView.getIndex();
        var oldContainer = studentsPlacedInIslands.get(index);
        if(oldContainer != null)
            oldContainer.addStudent(s);
        else
            studentsPlacedInIslands.put(index, new StudentsContainer().addStudent(s));

        //update island view
        islandView.addStudent(s);

        checkIfAllStudentsPlaced();
    }

    public void setIslands(List<ReducedIsland> islands) {
        islandsPane.getChildren().clear();
        islands.forEach(i -> islandsPane.addIsland(i));
    }

    public void setMyCoin(int coin) {
        myCoinLabel.setText(coin + "");
    }

    public void setPlayer2Board(ReducedPlayer player2, Map<Student, String> professors) {
        setPlayerBoard(player2, professors, player2NicknameLabel, player2Students, player2CoinLabel, player2TowerLabel, player2Tower);
    }

    public void setPlayer3Board(ReducedPlayer player3, Map<Student, String> professors) {
        setPlayerBoard(player3, professors, player3NicknameLabel, player3Students, player3CoinLabel, player3TowerLabel, player3Tower);
    }

    private void setPlayerBoard(ReducedPlayer player, Map<Student, String> professors, Label nicknameLabel, VBox playerStudents, Label playerCoinLabel, Label playerTowerLabel, TowerView playerTower) {
        playerStudents.getChildren().clear();

        nicknameLabel.setText(player.nickname());

        for (Student s : Student.values()) {
            var hbox = new HBox();
            hbox.setSpacing(5);
            var sv = new StudentView(s, player.nickname().equals(professors.get(s)));
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
        myNickname = Client.getInstance().getNickname();
        leaveButton.setText("ABBANDONA");
        gameTitlePopup.hide();
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
        currentGame = game;
        var myPlayer = findMyPlayer(game);
        var otherPlayers = new ArrayList<>(game.players());
        otherPlayers.remove(myPlayer);
        otherPlayers.sort(Comparator.comparing(ReducedPlayer::nickname));

        characterCards.setDisable(true);
        cloudsPane.setDisable(true);
        myStudentsBoard.setDisable(true);
        studentsPlacedInSchool = new StudentsContainer();
        studentsPlacedInIslands = new HashMap<>();

        setVisibilityForNumberOfPlayers(game.numberOfPlayers());
        setVisibilityForExpertMode(game.expertMode());
        setIslands(game.islands());
        setMotherNatureIndex(game.motherNaturePosition());
        cloudsPane.setClouds(game.currentRound().clouds());
        setMyBoard(myPlayer, game.currentProfessors());

        setPlayer2Board(otherPlayers.get(0), game.currentProfessors());

        //TODO: improve
        //set card played by me
        var cardPlayedByMe = game.currentRound()
                .playedAssistantCards()
                .get(myPlayer.nickname());
        myCard.setCard(cardPlayedByMe);

        //set card played by player 2
        var cardPlayedByPlayer2 = game.currentRound()
                .playedAssistantCards()
                .get(otherPlayers.get(0).nickname());
        player2Card.setCard(cardPlayedByPlayer2);

        //set player if present
        if (game.numberOfPlayers() > 2) {
            setPlayer3Board(otherPlayers.get(1), game.currentProfessors());

            //set card played by player 3
            var cardPlayedByPlayer3 = game.currentRound()
                    .playedAssistantCards()
                    .get(otherPlayers.get(1).nickname());
            player3Card.setCard(cardPlayedByPlayer3);
        }

        switch(game.currentState()) {
            case CREATED, STARTED -> gameTitlePopup.hide();
            case PAUSED -> {
                var offlinePlayers = "Giocatori offline: " +
                        game.players()
                                .stream()
                                .filter(p -> !p.isConnected())
                                .map(ReducedPlayer::nickname)
                                .collect(Collectors.joining(", "));

                gameTitlePopup.setState(GameTitlePopupView.State.PAUSED, offlinePlayers);
                gameTitlePopup.show();
            }
            case FINISHED -> {
                if(game.winner() == null) { //tie
                    gameTitlePopup.setState(GameTitlePopupView.State.TIE, "");
                } else {
                    if(game.winner().nickname().equals(myNickname)) {
                        gameTitlePopup.setState(GameTitlePopupView.State.WIN, "");
                    } else {
                        gameTitlePopup.setState(GameTitlePopupView.State.LOSE, game.winner().nickname()+ " ha vinto");
                    }
                }
                leaveButton.setText("VAI AL MENU PRINCIPALE");
                gameTitlePopup.show();
            }
            case TERMINATED -> {
                var leftPlayers = "Giocatori che hanno abbandonato: " +
                        game.players()
                                .stream()
                                .filter(p -> !p.isConnected())
                                .map(ReducedPlayer::nickname)
                                .collect(Collectors.joining(", "));

                gameTitlePopup.setState(GameTitlePopupView.State.TERMINATED, leftPlayers);
                leaveButton.setText("VAI AL MENU PRINCIPALE");
                gameTitlePopup.show();
            }
        }

        var currentPlayer = game.currentRound().currentPlayer();

        //my turn
        if (currentPlayer.equals(myPlayer)) {
            if (game.currentRound().stage() instanceof Stage.Attack s) { //attack
                var maxMotherNatureSteps =
                        cardPlayedByMe.motherNatureMaxMoves() +
                                game.currentRound().additionalMotherNatureMoves();

                switch (s) {
                    case STARTED -> {
                        //place students...
                        var studentsToMove =
                                game.numberOfPlayers() == 2 ?
                                        Constants.TwoPlayers.STUDENTS_TO_MOVE :
                                        Constants.ThreePlayers.STUDENTS_TO_MOVE;

                        setInfoString(InfoStrings.MY_TURN_PLACE_STUDENTS, studentsToMove);
                        myStudentsBoard.setDisable(false);
                        setIslandsForPlacingStudents();
                    }
                    case STUDENTS_PLACED -> {
                        //play character card or move mother nature
                        setInfoString(InfoStrings.MY_TURN_PLAY_CHARACTER_CARD);
                        characterCards.setDisable(false);

                        setMotherNaturePossibleSteps(game.motherNaturePosition(), maxMotherNatureSteps);
                    }
                    case CARD_PLAYED -> {
                        //move mother nature
                        setInfoString(InfoStrings.MY_TURN_MOVE_MOTHER_NATURE, maxMotherNatureSteps);
                        setMotherNaturePossibleSteps(game.motherNaturePosition(), maxMotherNatureSteps);
                    }
                    case MOTHER_NATURE_MOVED -> {
                        //select cloud
                        setInfoString(InfoStrings.MY_TURN_SELECT_CLOUD);
                        cloudsPane.setDisable(false);
                    }
                    case SELECTED_CLOUD -> {
                        Utils.LOGGER.info("This stage should not be reached");
                    }
                }
            } else { //plan
                setInfoString(InfoStrings.MY_TURN_PLAY_ASSISTANT_CARD);
                setAssistantDeckVisible(true);
            }
        } else {
            setInfoString(InfoStrings.OTHER_PLAYER_WAIT_FOR_HIS_TURN, currentPlayer.nickname());
        }
    }

    private void setIslandsForPlacingStudents() {
        for (var n : islandsPane.getChildren()) {
            var iv = (IslandView) n;
            iv.setDisable(false);

            iv.setOnMouseClicked(e -> {
                if(studentSelectContextMenu != null)
                    studentSelectContextMenu.hide();

                studentSelectContextMenu = new StudentSelectContextMenu(
                        currentGame.currentRound().currentPlayer().entrance(),
                        s -> placeStudentInIsland(s, iv)
                );

                studentSelectContextMenu.show(iv, e.getScreenX(), e.getScreenY());
            });
        }
    }

    private void setMyBoard(ReducedPlayer myPlayer, Map<Student, String> professors) {
        setMyStudentsBoard(myPlayer, professors);
        setMyCoin(myPlayer.coins());
        setMyTowers(myPlayer.towerColor(), myPlayer.towersCount());
        setAssistantCardsDeck(myPlayer.deck());
    }

    private ReducedPlayer findMyPlayer(ReducedGame game) {
        return game.players()
                .stream()
                .filter(p -> p.nickname().equals(myNickname))
                .findFirst()
                .orElse(null);
    }

    public void startPeekGame() {
        assistantCardsLayer.setOpacity(0.1);
    }

    public void endPeekGame() {
        assistantCardsLayer.setOpacity(1.0);
    }
}
