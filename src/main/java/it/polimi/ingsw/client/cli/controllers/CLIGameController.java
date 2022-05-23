package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.InfoString;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.*;
import it.polimi.ingsw.common.reducedmodel.ReducedCharacterCard;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.reducedmodel.charactercards.*;
import it.polimi.ingsw.common.requests.MoveMotherNatureRequest;
import it.polimi.ingsw.common.requests.PlaceStudentsRequest;
import it.polimi.ingsw.common.requests.PlayCharacterCardRequest;
import it.polimi.ingsw.common.requests.SelectCloudRequest;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CLIGameController implements ScreenController, Client.GameUpdateListener {
    private Client client;
    private Cursor cursor;

    private StudentsContainer studentsPlacedInSchool;
    private Map<Integer, StudentsContainer> studentsPlacedInIslands;

    @Override
    public void onShow() {
        client = Client.getInstance();
        cursor = Cursor.getInstance();

        cursor.clearScreen();
        client.addGameUpdateListener(this);
    }

    @Override
    public void onHide() {
        client.removeGameUpdateListener(this);
    }

    @Override
    public void onGameUpdate(ReducedGame game) {
        cursor.clearScreen();

        processGameState(game);
    }

    private void processGameState(ReducedGame game) {
        switch (game.currentState()) {
            case STARTED -> {
                drawGameView(game);
                var currentPlayer = game.currentRound().currentPlayer();
                if (currentPlayer.equals(client.getNickname())) {
                    processMyTurn(game);
                } else {
                    new InfoLabelView(InfoString.OTHER_PLAYER_WAIT_FOR_HIS_TURN, currentPlayer)
                            .draw();
                }
            }
            case PAUSED -> new TitleView(TitleView.Title.PAUSED,
                    "Giocatori offline: " + game.getOfflinePlayersList())
                    .draw();
            case TERMINATED -> new TitleView(TitleView.Title.TERMINATED,
                    "Giocatori che hanno abbandonato: " + game.getOfflinePlayersList())
                    .draw();
            case FINISHED -> {
                var winner = game.winner();
                if (winner == null)
                    new TitleView(TitleView.Title.TIE).draw();
                else if (client.getNickname().equals(winner))
                    new TitleView(TitleView.Title.WIN).draw();
                else
                    new TitleView(TitleView.Title.LOSE, winner + " ha vinto").draw();
            }
        }

        //if game ended ask user for new game
        if(game.currentState() == Game.State.TERMINATED ||
                game.currentState() == Game.State.FINISHED) {

            var input = new BooleanInputView("Vuoi giocare ancora? [s/n]");
            input.setListener(r -> {
                if (Boolean.TRUE.equals(r))
                    client.leaveGame();
                else
                    client.exitApp();
            });
            input.draw();
        }
    }

    private void processMyTurn(ReducedGame game) {
        var stage = game.currentRound().stage();

        if (stage == Stage.Plan.PLAN) {
            new AssistantCardsView(game.getMyPlayer(client.getNickname()).deck(), null)
                    .draw();
        } else {
            switch ((Stage.Attack) stage) {
                case STARTED -> processPlaceStudents(game);
                case STUDENTS_PLACED -> {
                    var myPlayer = game.getMyPlayer(client.getNickname());
                    var myCoins = myPlayer.coins();

                    var playableCards =
                            game.getOrderedCharacterCards()
                                    .stream()
                                    .filter(e -> myCoins >= e.getKey().getCost(e.getValue()) &&
                                            (e.getKey() != Character.MINSTREL || myPlayer.school().getSize() >= 2))
                                    .toList();

                    if(playableCards.isEmpty() || !game.expertMode())
                        processMoveMotherNature(game);
                    else
                        askForPlayingCharacterCard(game, playableCards);
                }
                case CARD_PLAYED -> processMoveMotherNature(game);
                case MOTHER_NATURE_MOVED -> processSelectCloud(game);
            }
        }
    }

    private void askForPlayingCharacterCard(ReducedGame game, List<Map.Entry<Character, Integer>> playableCards) {
        var simpleList = playableCards.stream()
                .map(Map.Entry::getKey)
                .map(Constants.CHARACTER_NAMES::get)
                .collect(Collectors.joining(", "));

        var input = new BooleanInputView("Vuoi giocare un personaggio? [s/n] (" + simpleList + ")");
        input.setListener(r -> {
            if (Boolean.TRUE.equals(r))
                processPlayCharacterCard(game, playableCards);
            else
                processMoveMotherNature(game);
        });
        input.draw();
    }

    /**
     * Ask user what character card to play
     */
    private void processPlayCharacterCard(ReducedGame game, List<Map.Entry<Character, Integer>> playableCards) {
        var characterCardsView = new CharacterCardsView(playableCards);
        characterCardsView.setListener(c -> {
            drawGameView(game);

            ReducedCharacterCard card = null;
            switch (c) {
                case CENTAUR -> card = new ReducedCentaurCharacterCard();
                case FARMER -> card = new ReducedFarmerCharacterCard();
                case POSTMAN -> card = new ReducedPostmanCharacterCard();
                case KNIGHT -> card = new ReducedKnightCharacterCard();
                default -> processComplexCharacterCard(game, c);
            }

            if(card != null)
                client.forwardGameRequest(new PlayCharacterCardRequest(card));
        });
        characterCardsView.draw();
    }

    private void processComplexCharacterCard(ReducedGame game, Character c) {
        switch (c) {
            case GRANDMA, HERALD -> {
                var input = new IntegerInputView("Su quale isola vuoi applicare la carta:", 0, game.islands().size() - 1);
                input.setListener(r -> client.forwardGameRequest(new PlayCharacterCardRequest(c == Character.GRANDMA ?
                        new ReducedGrandmaCharacterCard(r) : new ReducedHeraldCharacterCard(r))
                ));
                input.draw();
            }
            case MINSTREL -> {

            }
            case MUSHROOM_MAN -> {
                var input = new CommandInputView("Seleziona studente");
                CommandInputView.CommandListener listener = (cmd, args) -> {
                    var stud = Student.YELLOW;
                    for(var s : Student.values())
                        if(s.name().startsWith(cmd))
                            stud = s;
                    client.forwardGameRequest(new PlayCharacterCardRequest(new ReducedMushroomManCharacterCard(stud)));
                };
                input.addCommandListener("y", "Giallo", listener);
                input.addCommandListener("b", "Blu", listener);
                input.addCommandListener("g", "Verde", listener);
                input.addCommandListener("r", "Rosso", listener);
                input.addCommandListener("p", "Rosa", listener);
                input.draw();
            }
        }
    }

    private void drawGameView(ReducedGame game) {
        new IslandsLayoutView(game.islands(), game.motherNaturePosition()).draw();
        new DashoardView(game).draw();
        new CloudsLayoutView(game.currentRound().clouds()).draw();
        cursor.moveToXY(1, 22);
    }

    /**
     * Ask user where to vove mother nature
     * @param game
     */
    private void processMoveMotherNature(ReducedGame game) {
        var maxSteps = game.calculateMaxMotherNatureSteps();
        var input = new IntegerInputView(
                "Di quanti passi vuoi far muovere madre natura (max " + maxSteps + " passi)?",
                0,
                maxSteps
        );

        input.setListener(pos -> client.forwardGameRequest(
                new MoveMotherNatureRequest(pos),
                e -> input.showError(e.getMessage())
        ));
        input.draw();
    }


    /**
     * Check if all students were placed in the placing students phase
     */
    private int checkIfAllStudentsPlaced(ReducedGame currentGame){
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
        }

        return count;
    }

    private void processPlaceStudents(ReducedGame game) {
        studentsPlacedInSchool = new StudentsContainer();
        studentsPlacedInIslands = new HashMap<>();
        int studentsToMove = game.numberOfPlayers() == 2 ?
                Constants.TwoPlayers.STUDENTS_TO_MOVE : Constants.ThreePlayers.STUDENTS_TO_MOVE;
        var placeStudents = new CommandInputView("Muovi studenti (0/%d)".formatted(studentsToMove));
        placeStudents.addCommandListener("sala", "Sala x B (sposta x studenti blu in sala)", ((command, args) -> {
            if (args.size() != 2) {
                placeStudents.showError("Inserisci 2 argomenti");
                return;
            }
            int n = 0;
            try { 
                n = Integer.parseInt(args.get(0));
            } catch (Exception e) {
                placeStudents.showError("Inserisci un numero valido");
                return;
            }
            String color = args.get(1).toLowerCase().strip();
            switch (color) {
                case "b":
                    studentsPlacedInSchool.addStudents(Student.BLUE, n);
                    break;
                case "g":
                    studentsPlacedInSchool.addStudents(Student.YELLOW, n);
                    break;
                case "v":
                    studentsPlacedInSchool.addStudents(Student.GREEN, n);
                    break;
                case "p":
                    studentsPlacedInSchool.addStudents(Student.PINK, n);
                    break;
                case "r":
                    studentsPlacedInSchool.addStudents(Student.RED, n);
                    break;
                default:
                    placeStudents.showError("Inserisci un colore valido");
                    return;
            }

            int remaining = checkIfAllStudentsPlaced(game);
            placeStudents.setMessage("Muovi studenti (%d/%d)".formatted(remaining, studentsToMove));
        }));

        placeStudents.draw();

        /*client.forwardGameRequest(
                new PlaceStudentsRequest(
                        new RandomizedStudentsContainer(game.getMyPlayer(client.getNickname()).entrance()).pickManyRandom(3),
                        new HashMap<>()
                ),
                e -> {  }
        );
        */
    }

    private void processSelectCloud(ReducedGame game) {
        // if only 1 cloud is available, select it
        if(game.currentRound().clouds().size() == 1){
            client.forwardGameRequest(
                    new SelectCloudRequest(game.currentRound().clouds().get(0)),
                    e -> cursor.print(e.getMessage(), 1, 23) //should never be called
            );
        } else {
            var input = new IntegerInputView("Seleziona una nuvola", 0, game.currentRound().clouds().size() - 1);
            input.setListener(cloud -> client.forwardGameRequest(
                    new SelectCloudRequest(game.currentRound().clouds().get(cloud)),
                    e -> input.showError(e.getMessage())
            ));
            input.draw();
        }
    }
}
