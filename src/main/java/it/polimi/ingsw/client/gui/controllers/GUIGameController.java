package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.InfoString;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.gui.customviews.*;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.common.reducedmodel.charactercards.*;
import it.polimi.ingsw.common.requests.*;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for controlling the game screen.
 */
public class GUIGameController implements ScreenController, Client.GameUpdateListener {
    @FXML
    private IslandsCircularPane islandsPane;
    @FXML
    private CloudsPane cloudsPane;
    @FXML
    private VBox characterCardsView;
    @FXML
    private HBox characterCards;
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
    private StudentSelectContextMenu studentSelectContextMenu;
    private StudentsContainer studentsSwapToAdd;
    private StudentsContainer studentsSwapToRemove;
    
    private Client client;

    /**
     * This method sets the assistant cards deck of my player
     * @param deck
     */
    private void setAssistantCardsDeck(Map<AssistantCard, Boolean> deck) {
        assistantCardsDeck.setDeck(deck);
        assistantCardsDeck.setOnCardSelectedListener(card -> client.forwardGameRequest(
                new PlayAssistantCardRequest(card),
                () -> assistantCardsLayer.setVisible(false),
                err -> assistantCardsDeck.showError("Non puoi giocare questa carta")
        ));
    }

    @FXML
    private void onLeavePressed() {
        client.leaveGame();
    }

    /**
     * This method shows or hides components based on the current game expert mode
     * @param expertMode
     */
    public void setVisibilityForExpertMode(boolean expertMode) {
        player2BoardView.setVisibilityForExpertMode(expertMode);
        player3BoardView.setVisibilityForExpertMode(expertMode);

        myCoin.setVisible(expertMode);
        myCoin.setManaged(expertMode);

        characterCardsView.setVisible(expertMode);
        characterCardsView.setManaged(expertMode);
    }

    /**
     * This method shows or hides components based on the number of players of the current game
     * @param numberOfPlayers
     */
    public void setVisibilityForNumberOfPlayers(int numberOfPlayers) {
        player3BoardView.setVisible(numberOfPlayers == 3);
        player3BoardView.setManaged(numberOfPlayers == 3);
    }

    /**
     * Check if all students were placed in the placing students phase
     */
    private void checkIfAllStudentsPlaced(){
        var count = 0;
        count += studentsPlacedInSchool.getSize();
        for(StudentsContainer sc : studentsPlacedInIslands.values())
            count += sc.getSize();

        var studentsToMove = currentGame.numberOfPlayers() == 2 ?
                Constants.TwoPlayers.STUDENTS_TO_MOVE : Constants.ThreePlayers.STUDENTS_TO_MOVE;

        if(count == studentsToMove) {
            client
                    .forwardGameRequest(
                            new PlaceStudentsRequest(
                                    studentsPlacedInSchool,
                                    studentsPlacedInIslands
                            ),
                            err -> Utils.LOGGER.info("Error placing students: " + err)
                    );
        } else {
            infoLabel.setInfoString(InfoString.MY_TURN_PLACE_STUDENTS, studentsToMove-count);
            myStudentsBoard.setPlayer(myPlayer);
            myStudentsBoard.setProfessors(currentGame.currentProfessors());
        }
    }

    /**
     * Place student in school
     * @param s
     */
    private void placeStudentInSchool(Student s) {
        if(myPlayer.entrance().getCountForStudent(s) <= 0) return;

        myPlayer.entrance().removeStudent(s);
        myPlayer.school().addStudent(s);

        studentsPlacedInSchool.addStudent(s);

        checkIfAllStudentsPlaced();
    }

    /**
     * Place student in island
     * @param s
     * @param islandView
     */
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
        client = Client.getInstance();
        client.addGameUpdateListener(this);

        //reset view
        resetView();
    }

    @Override
    public void onHide() {
        client.removeGameUpdateListener(this);
    }

    @Override
    public void onGameUpdate(ReducedGame game) {
        //run game update on the FX thread
        Platform.runLater(() -> gameUpdate(game));
    }

    /**
     * Update views based on the game update
     * @param game
     */
    private void gameUpdate(ReducedGame game) {
        currentGame = game;
        myPlayer = game.getMyPlayer(myNickname);
        otherPlayers = game.getOtherPlayers(myNickname);

        resetView();

        setVisibilityForNumberOfPlayers(game.numberOfPlayers());
        setVisibilityForExpertMode(game.expertMode());
        islandsPane.setIslands(game.islands());
        islandsPane.setMotherNaturePosition(game.motherNaturePosition());
        cloudsPane.setClouds(game.currentRound().clouds(), c -> client
                .forwardGameRequest(
                        new SelectCloudRequest(c),
                        err -> Utils.LOGGER.info("Error selecting cloud " + err.getMessage())
                ));

        //set character cards
        setCharacterCards();

        //set players boards
        setMyBoard();
        setPlayer2Board();
        if (game.numberOfPlayers() > 2)
            setPlayer3Board();


        processGameState();

        //my turn
        if (game.currentRound().currentPlayer().equals(myPlayer.nickname())) {
            processMyTurn();
        } else {
            infoLabel.setInfoString(InfoString.OTHER_PLAYER_WAIT_FOR_HIS_TURN, game.currentRound().currentPlayer());
        }
    }

    /**
     * Set up character cards
     */
    private void setCharacterCards() {
        int i = 0;
        var orderedCharacterCards =
                currentGame.characterCards().entrySet()
                        .stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getCost()))
                        .toList();

        for(var e : orderedCharacterCards) {
            var ccv = (CharacterCardView) characterCards.getChildren().get(i);
            var usedTimes = e.getValue();
            var hasEnoughCoins = myPlayer.coins() >= e.getKey().getCost(usedTimes);

            //minstrel (implies) hasEnough students
            //the minstrel card requires at least 2 students in entrance and school
            var hasEnoughStudents = (e.getKey() != Character.MINSTREL) || myPlayer.school().getSize() >= 2;

            var canPlay = hasEnoughCoins && hasEnoughStudents;

            ccv.setCharacter(e.getKey(), usedTimes);
            ccv.setDisable(!canPlay);
            ccv.setGrayedOut(!canPlay);
            ccv.setOnMouseClicked(event -> processCharacterCardClick(e.getKey(), ccv, event));
            i++;
        }
    }

    private void processCharacterCardClick(Character character, CharacterCardView ccv, MouseEvent event) {
        characterCards.setDisable(true);
        switch (character) {
            case CENTAUR, FARMER, KNIGHT, POSTMAN -> {
                //send directly the request
                var card = switch(character) {
                    case CENTAUR -> new ReducedCentaurCharacterCard();
                    case FARMER -> new ReducedFarmerCharacterCard();
                    case KNIGHT -> new ReducedKnightCharacterCard();
                    case POSTMAN -> new ReducedPostmanCharacterCard();
                    default -> null;
                };
                client.forwardGameRequest(new PlayCharacterCardRequest(card));
            }
            case GRANDMA, HERALD -> {
                //select island
                islandsPane.arrangeIslandsForPlayingCharacterCard(iv -> client.forwardGameRequest(
                    new PlayCharacterCardRequest(character == Character.GRANDMA ?
                            new ReducedGrandmaCharacterCard(iv.getIndex()) :
                            new ReducedHeraldCharacterCard(iv.getIndex()))
                ));
                infoLabel.setInfoString(character == Character.GRANDMA ?
                        InfoString.MY_TURN_SELECT_ISLAND_FOR_GRANDMA :
                        InfoString.MY_TURN_SELECT_ISLAND_FOR_HERALD);
            }
            case MUSHROOM_MAN -> {
                if(studentSelectContextMenu != null)
                    studentSelectContextMenu.hide();
                //select student
                studentSelectContextMenu = new StudentSelectContextMenu(
                        s -> client.forwardGameRequest(
                                new PlayCharacterCardRequest(new ReducedMushroomManCharacterCard(s))
                        )
                );

                studentSelectContextMenu.show(ccv, event.getScreenX(), event.getScreenY());
            }
            case MINSTREL -> {
                //swap students
                infoLabel.setInfoString(InfoString.MY_TURN_SELECT_STUDENT_TO_SWAP_FROM_SCHOOL_TO_ENTRANCE, 2);
                myStudentsBoard.setStudentsClickDisable(false);
                myStudentsBoard.setOnStudentClickListener(this::addStudentToSwap);
            }
        }
    }

    /**
     * This method is called when the user played Minstrel character card and clicked on a student to select.
     * @param s
     */
    private void addStudentToSwap(Student s) {
        if(studentsSwapToRemove.getSize() < 2) {
            if(myPlayer.school().getCountForStudent(s) <= 0) return;

            //remove from school and add to entrance
            myPlayer.school().removeStudent(s);
            myPlayer.entrance().addStudent(s);
            studentsSwapToRemove.addStudent(s);

            //update my students board
            myStudentsBoard.setPlayer(myPlayer);

            if(studentsSwapToRemove.getSize() == 2)
                infoLabel.setInfoString(InfoString.MY_TURN_SELECT_STUDENT_TO_SWAP_FROM_ENTRANCE_TO_SCHOOL, 2);
            else
                infoLabel.setInfoString(
                        InfoString.MY_TURN_SELECT_STUDENT_TO_SWAP_FROM_SCHOOL_TO_ENTRANCE,
                        2 - studentsSwapToRemove.getSize()
                );
        }else if(studentsSwapToAdd.getSize() < 2) {
            if(myPlayer.entrance().getCountForStudent(s) <= 0) return;

            //add to school and remove from entrance
            myPlayer.entrance().removeStudent(s);
            myPlayer.school().addStudent(s);
            studentsSwapToAdd.addStudent(s);
            //update my students board
            myStudentsBoard.setPlayer(myPlayer);

            if(studentsSwapToAdd.getSize() == 2)
                client.forwardGameRequest(new PlayCharacterCardRequest(
                        new ReducedMinstrelCharacterCard(studentsSwapToAdd, studentsSwapToRemove))
                );
            else
                infoLabel.setInfoString(
                        InfoString.MY_TURN_SELECT_STUDENT_TO_SWAP_FROM_ENTRANCE_TO_SCHOOL,
                        2 - studentsSwapToAdd.getSize()
                );

        }
    }

    /**
     * Update the views when it's my turn
     */
    private void processMyTurn() {
        if (currentGame.currentRound().stage() instanceof Stage.Attack s) { //attack
            var cardPlayed = currentGame.currentRound()
                    .playedAssistantCards()
                    .get(myPlayer.nickname());

            var maxMotherNatureSteps = currentGame.calculateMaxMotherNatureSteps();

            switch (s) {
                case STARTED -> {
                    //place students...
                    var studentsToMove =
                            currentGame.numberOfPlayers() == 2 ?
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

                    arrangeIslandsForMotherNatureMovement(currentGame.motherNaturePosition(), maxMotherNatureSteps);
                }
                case CARD_PLAYED -> {
                    //move mother nature
                    infoLabel.setInfoString(InfoString.MY_TURN_MOVE_MOTHER_NATURE, maxMotherNatureSteps);
                    arrangeIslandsForMotherNatureMovement(currentGame.motherNaturePosition(), maxMotherNatureSteps);
                }
                case MOTHER_NATURE_MOVED -> {
                    //select cloud
                    infoLabel.setInfoString(InfoString.MY_TURN_SELECT_CLOUD);
                    cloudsPane.setDisable(false);
                }
                case SELECTED_CLOUD -> {
                    //do nothing
                }
            }
        } else { //plan
            infoLabel.setInfoString(InfoString.MY_TURN_PLAY_ASSISTANT_CARD);
            assistantCardsLayer.setVisible(true);
        }
    }

    /**
     * Update the views based on the game state
     */
    private void processGameState() {
        switch(currentGame.currentState()) {
            case CREATED, STARTED -> gameTitlePopup.hide();
            case PAUSED -> {
                gameTitlePopup.setState(GameTitlePopupView.State.PAUSED,
                        "Giocatori offline: " + currentGame.getOfflinePlayersList());
                gameTitlePopup.show();
            }
            case FINISHED -> {
                if(currentGame.winner() == null) { //tie
                    gameTitlePopup.setState(GameTitlePopupView.State.TIE, "");
                } else {
                    if(currentGame.winner().equals(myNickname)) {
                        gameTitlePopup.setState(GameTitlePopupView.State.WIN, "");
                    } else {
                        gameTitlePopup.setState(GameTitlePopupView.State.LOSE, currentGame.winner()+ " ha vinto");
                    }
                }
                leaveButton.setText("VAI AL MENU PRINCIPALE");
                gameTitlePopup.show();
            }
            case TERMINATED -> {
                gameTitlePopup.setState(GameTitlePopupView.State.TERMINATED,
                        "Giocatori che hanno abbandonato: " + currentGame.getOfflinePlayersList());
                leaveButton.setText("VAI AL MENU PRINCIPALE");
                gameTitlePopup.show();
            }
        }
    }

    /**
     * Set up my board
     */
    private void setMyBoard() {
        myPlayerBoardView.setPlayer(myPlayer);
        myStudentsBoard.setPlayer(myPlayer);
        myStudentsBoard.setProfessors(currentGame.currentProfessors());
        myStudentsBoard.setOnStudentClickListener(this::placeStudentInSchool);
        myCoinLabel.setText(String.valueOf(myPlayer.coins()));
        myTower.setTowerColor(myPlayer.towerColor());
        myTowerLabel.setText(String.valueOf(myPlayer.towersCount()));
        setAssistantCardsDeck(myPlayer.deck());

        var cardPlayedByMe = currentGame.currentRound()
                .playedAssistantCards()
                .get(myPlayer.nickname());
        myPlayerBoardView.setPlayedCard(cardPlayedByMe);
    }

    /**
     * Setup player 2 board
     */
    private void setPlayer2Board() {
        player2BoardView.setPlayer(otherPlayers.get(0));
        player2BoardView.setProfessors(currentGame.currentProfessors());

        var cardPlayedByPlayer2 = currentGame.currentRound()
                .playedAssistantCards()
                .get(otherPlayers.get(0).nickname());
        player2BoardView.setPlayedCard(cardPlayedByPlayer2);
    }

    /**
     * Setup player 3 board
     */
    private void setPlayer3Board() {
        player3BoardView.setPlayer(otherPlayers.get(1));
        player3BoardView.setProfessors(currentGame.currentProfessors());

        var cardPlayedByPlayer3 = currentGame.currentRound()
                .playedAssistantCards()
                .get(otherPlayers.get(1).nickname());
        player3BoardView.setPlayedCard(cardPlayedByPlayer3);
    }

    /**
     * Reset views
     */
    private void resetView() {
        myNickname = client.getNickname();
        leaveButton.setText("ABBANDONA");
        gameTitlePopup.hide();
        characterCards.setDisable(true);
        cloudsPane.setDisable(true);
        myStudentsBoard.setStudentsClickDisable(true);
        studentsPlacedInSchool = new StudentsContainer();
        studentsPlacedInIslands = new HashMap<>();
        assistantCardsLayer.setVisible(false);
        studentsSwapToAdd = new StudentsContainer();
        studentsSwapToRemove = new StudentsContainer();
    }

    /**
     * Arrange islands for the move mother nature phase
     * @param motherNaturePosition
     * @param maxMotherNatureSteps
     */
    private void arrangeIslandsForMotherNatureMovement(int motherNaturePosition, int maxMotherNatureSteps) {
        islandsPane.arrangeIslandsForMotherNatureMovement(motherNaturePosition, maxMotherNatureSteps,
                s -> client.forwardGameRequest(
                        new MoveMotherNatureRequest(s),
                        err -> Utils.LOGGER.info("Error moving mother nature: " + err.getMessage())
                ));
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
