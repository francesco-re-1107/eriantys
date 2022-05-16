package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.InfoString;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.gui.customviews.*;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.common.requests.MoveMotherNatureRequest;
import it.polimi.ingsw.common.requests.PlaceStudentsRequest;
import it.polimi.ingsw.common.requests.PlayAssistantCardRequest;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Stage;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController implements ScreenController, Client.GameUpdateListener {
    @FXML
    private IslandCircularPane islandsPane;
    @FXML
    private CloudsPane cloudsPane;
    @FXML
    private VBox characterCardsView;
    @FXML
    private HBox characterCards;
    @FXML
    private CharacterCardView characterCard1;
    @FXML
    private CharacterCardView characterCard2;
    @FXML
    private CharacterCardView characterCard3;
    @FXML
    private AssistantCardDeckView assistantCardsDeck;
    @FXML
    private VBox assistantCardsLayer;
    @FXML
    private GameTitlePopupView gameTitlePopup;
    @FXML
    public PlayerBoardView myPlayerBoardView;
    @FXML
    private PlayerBoardView player2BoardView;
    @FXML
    private PlayerBoardView player3BoardView;
    @FXML
    private Button leaveButton;
    @FXML
    private Label myTowerLabel;
    @FXML
    private HBox myCoin;
    @FXML
    private TowerView myTower;
    @FXML
    private InfoLabel infoLabel;
    @FXML
    private StudentsBoardView myStudentsBoard;
    @FXML
    private Label myCoinLabel;
    private ReducedGame currentGame;

    private StudentsContainer studentsPlacedInSchool;
    private Map<Integer, StudentsContainer> studentsPlacedInIslands;
    private String myNickname;
    private ReducedPlayer myPlayer;
    private List<ReducedPlayer> otherPlayers;

    private void setAssistantCardsDeck(Map<AssistantCard, Boolean> deck) {
        assistantCardsDeck.setDeck(deck);
        assistantCardsDeck.setOnCardSelected(card -> Client.getInstance().forwardGameRequest(
                new PlayAssistantCardRequest(card),
                () -> assistantCardsLayer.setVisible(false),
                err -> assistantCardsDeck.showError("Non puoi giocare questa carta")
        ));
    }

    @FXML
    private void onLeavePressed() {
        Client.getInstance().leaveGame();
    }

    public void setVisibilityForExpertMode(boolean expertMode) {
        player2BoardView.setVisibilityForExpertMode(expertMode);
        player3BoardView.setVisibilityForExpertMode(expertMode);

        myCoin.setVisible(expertMode);
        myCoin.setManaged(expertMode);

        characterCardsView.setVisible(expertMode);
        characterCardsView.setManaged(expertMode);
    }

    public void setVisibilityForNumberOfPlayers(int numberOfPlayers) {
        player3BoardView.setVisible(numberOfPlayers == 3);
        player3BoardView.setManaged(numberOfPlayers == 3);
    }

    private void checkIfAllStudentsPlaced(){
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
        } else {
            infoLabel.setInfoString(InfoString.MY_TURN_PLACE_STUDENTS, studentsToMove-count);
            myStudentsBoard.setPlayer(myPlayer);
            myStudentsBoard.setProfessors(currentGame.currentProfessors());
        }
    }

    private void placeStudentInSchool(Student s) {
        if(myPlayer.entrance().getCountForStudent(s) <= 0) return;

        myPlayer.entrance().removeStudent(s);
        myPlayer.school().addStudent(s);

        studentsPlacedInSchool.addStudent(s);

        checkIfAllStudentsPlaced();
    }

    private void placeStudentInIsland(Student s, IslandView islandView) {
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

    @Override
    public void onShow() {
        Client.getInstance().addGameUpdateListener(this);

        //reset view
        myNickname = Client.getInstance().getNickname();
        leaveButton.setText("ABBANDONA");
        gameTitlePopup.hide();
        assistantCardsLayer.setVisible(false);
        myStudentsBoard.setStudentsClickDisable(true);
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

        myPlayer = findMyPlayer(game);
        otherPlayers = game.players().stream()
                .filter(p -> !p.nickname().equals(myNickname))
                .sorted(Comparator.comparing(ReducedPlayer::nickname))
                .toList();

        characterCards.setDisable(true);
        cloudsPane.setDisable(true);
        myStudentsBoard.setStudentsClickDisable(true);
        studentsPlacedInSchool = new StudentsContainer();
        studentsPlacedInIslands = new HashMap<>();
        assistantCardsLayer.setVisible(false);

        setVisibilityForNumberOfPlayers(game.numberOfPlayers());
        setVisibilityForExpertMode(game.expertMode());
        islandsPane.setIslands(game.islands());
        islandsPane.setMotherNaturePosition(game.motherNaturePosition());
        cloudsPane.setClouds(game.currentRound().clouds());
        setMyBoard(myPlayer, game.currentProfessors());

        myPlayerBoardView.setPlayer(myPlayer);

        player2BoardView.setPlayer(otherPlayers.get(0));
        player2BoardView.setProfessors(game.currentProfessors());

        //TODO: improve
        //set card played by me
        var cardPlayedByMe = game.currentRound()
                .playedAssistantCards()
                .get(myPlayer.nickname());
        myPlayerBoardView.setPlayedCard(cardPlayedByMe);

        //set card played by player 2
        var cardPlayedByPlayer2 = game.currentRound()
                .playedAssistantCards()
                .get(otherPlayers.get(0).nickname());
        player2BoardView.setPlayedCard(cardPlayedByPlayer2);

        //set player if present
        if (game.numberOfPlayers() > 2) {
            player3BoardView.setPlayer(otherPlayers.get(1));
            player3BoardView.setProfessors(game.currentProfessors());

            //set card played by player 3
            var cardPlayedByPlayer3 = game.currentRound()
                    .playedAssistantCards()
                    .get(otherPlayers.get(1).nickname());
            player3BoardView.setPlayedCard(cardPlayedByPlayer3);
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
                    if(game.winner().equals(myNickname)) {
                        gameTitlePopup.setState(GameTitlePopupView.State.WIN, "");
                    } else {
                        gameTitlePopup.setState(GameTitlePopupView.State.LOSE, game.winner()+ " ha vinto");
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
        if (currentPlayer.equals(myPlayer.nickname())) {
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

                        infoLabel.setInfoString(InfoString.MY_TURN_PLACE_STUDENTS, studentsToMove);
                        myStudentsBoard.setStudentsClickDisable(false);
                        islandsPane.arrangeIslandsForPlacingStudents(myPlayer, this::placeStudentInIsland);
                    }
                    case STUDENTS_PLACED -> {
                        //play character card or move mother nature
                        infoLabel.setInfoString(InfoString.MY_TURN_PLAY_CHARACTER_CARD, maxMotherNatureSteps);
                        characterCards.setDisable(false);

                        arrangeIslandsForMotherNatureMovement(game.motherNaturePosition(), maxMotherNatureSteps);
                    }
                    case CARD_PLAYED -> {
                        //move mother nature
                        infoLabel.setInfoString(InfoString.MY_TURN_MOVE_MOTHER_NATURE, maxMotherNatureSteps);
                        arrangeIslandsForMotherNatureMovement(game.motherNaturePosition(), maxMotherNatureSteps);
                    }
                    case MOTHER_NATURE_MOVED -> {
                        //select cloud
                        infoLabel.setInfoString(InfoString.MY_TURN_SELECT_CLOUD);
                        cloudsPane.setDisable(false);
                    }
                    case SELECTED_CLOUD -> {}//do nothing
                }
            } else { //plan
                infoLabel.setInfoString(InfoString.MY_TURN_PLAY_ASSISTANT_CARD);
                assistantCardsLayer.setVisible(true);
            }
        } else {
            infoLabel.setInfoString(InfoString.OTHER_PLAYER_WAIT_FOR_HIS_TURN, currentPlayer);
        }
    }

    private void arrangeIslandsForMotherNatureMovement(int motherNaturePosition, int maxMotherNatureSteps) {
        islandsPane.arrangeIslandsForMotherNatureMovement(motherNaturePosition, maxMotherNatureSteps,
                s -> Client.getInstance().forwardGameRequest(
                        new MoveMotherNatureRequest(s),
                        () -> {},
                        err -> Utils.LOGGER.info("Error moving mother nature: " + err.getMessage())
                ));
    }

    private void setMyBoard(ReducedPlayer myPlayer, Map<Student, String> professors) {
        myStudentsBoard.setPlayer(myPlayer);
        myStudentsBoard.setProfessors(professors);
        myStudentsBoard.setOnStudentClickListener(this::placeStudentInSchool);
        myCoinLabel.setText(String.valueOf(myPlayer.coins()));
        myTower.setTowerColor(myPlayer.towerColor());
        myTowerLabel.setText(String.valueOf(myPlayer.towersCount()));
        setAssistantCardsDeck(myPlayer.deck());
    }

    /**
     * Find my player in the given game
     * @param game
     * @return my player
     */
    private ReducedPlayer findMyPlayer(ReducedGame game) {
        return game.players()
                .stream()
                .filter(p -> p.nickname().equals(myNickname))
                .findFirst()
                .orElse(null);
    }

    /**
     * This method hide temporarily the assistant card deck to show the game
     */
    public void startPeekGame() {
        assistantCardsLayer.setOpacity(0.1);
    }

    /**
     * This method is called after the startPeekGame to show again the assistant card deck
     */
    public void endPeekGame() {
        assistantCardsLayer.setOpacity(1.0);
    }
}
