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

/**
 * This class is responsible for controlling the game screen of the CLI.
 */
public class CLIGameController implements ScreenController, Client.GameUpdateListener {

    /**
     * Reference to the client class
     */
    private Client client;

    /**
     * Reference to the cursor
     */
    private Cursor cursor;

    /**
     * Students placed in school
     * Used for students placement
     */
    private StudentsContainer studentsPlacedInSchool;

    /**
     * Students placed on islands
     * Used for students placement
     */
    private Map<Integer, StudentsContainer> studentsPlacedInIslands;

    private boolean finishedPlacingStudents;

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
        //when a game update is received clear screen and input
        cursor.clearScreen();
        cursor.clearInput();

        processGameState(game);
    }

    /**
     * Processes the game update just received based on the game state
     * @param game
     */
    private void processGameState(ReducedGame game) {
        switch (game.currentState()) {
            case CREATED -> { /*do nothing*/ }
            case STARTED -> {
                //draw the game
                drawGameView(game);

                //check if it's my turn
                var currentPlayer = game.currentRound().currentPlayer();
                if (currentPlayer.equals(client.getNickname())) {
                    processMyTurn(game);
                } else {
                    //not my turn
                    new InfoLabelView(InfoString.OTHER_PLAYER_WAIT_FOR_HIS_TURN, currentPlayer)
                            .draw();
                }
            }
            case PAUSED -> {
                //show paused title
                new TitleView(TitleView.Title.PAUSED,
                        "Giocatori offline: " + game.getOfflinePlayersList()).draw();
                var input = new SimpleInputView("Premi invio per abbandonare la partita");
                input.setListener(r -> client.leaveGame());
                input.draw();
            }
            case TERMINATED -> new TitleView(TitleView.Title.TERMINATED,
                    "Giocatori che hanno abbandonato: " + game.getOfflinePlayersList())
                    .draw();
            case FINISHED -> {
                //show finished title
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

    /**
     * If my turn than check current stage
     * @param game the game update
     */
    private void processMyTurn(ReducedGame game) {
        var stage = game.currentRound().stage();

        if (stage == Stage.Plan.PLAN) {
            //if plan stage then ask user which assistant card to play
            new AssistantCardsView(game.getMyPlayer(client.getNickname()).deck())
                    .draw();
        } else {
            //attack stage
            switch ((Stage.Attack) stage) {
                case STARTED -> processPlaceStudents(game);
                case STUDENTS_PLACED -> {
                    //check if there are character cards to play
                    var myPlayer = game.getMyPlayer(client.getNickname());
                    var myCoins = myPlayer.coins();

                    var playableCards =
                            game.getOrderedCharacterCards()
                                    .stream()
                                    .filter(e -> myCoins >= e.getKey().getCost(e.getValue()) &&
                                            (e.getKey() != Character.MINSTREL || myPlayer.school().getSize() >= 2))
                                    .toList();

                    //if there aren't any playable cards or the game is not with expert mode
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

    /**
     * Ask user if he wants to play a character card
     * If true, ask user which card to play and play it, otherwise move mother nature
     * @param game the game update
     * @param playableCards the list of playable character cards
     */
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

            //simple cards are played immediately, otherwise process it in another method
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

    /**
     * This method handles complex character cards, aka character cards that need input from the user.
     * @param game the game update
     * @param c the character to play
     */
    private void processComplexCharacterCard(ReducedGame game, Character c) {

        switch (c) {
            case GRANDMA, HERALD -> { //ask island
                var input = new IntegerInputView("Su quale isola vuoi applicare la carta:", 0, game.islands().size() - 1);
                input.setListener(r -> client.forwardGameRequest(new PlayCharacterCardRequest(c == Character.GRANDMA ?
                        new ReducedGrandmaCharacterCard(r) : new ReducedHeraldCharacterCard(r))
                ));
                input.draw();
            }
            case MINSTREL -> { //swap two students from entrance to school
                new MinstrelInputView(
                        toRemove -> drawGameView(game),
                        (toRemove, toAdd) -> {
                            client.forwardGameRequest(new PlayCharacterCardRequest(
                                    new ReducedMinstrelCharacterCard(toAdd, toRemove))
                            );
                        },
                    game.getMyPlayer(client.getNickname()).entrance(),
                    game.getMyPlayer(client.getNickname()).school()
                ).draw();
            }
            case MUSHROOM_MAN -> { //select a student color not influencing
                var input = new CommandInputView("Seleziona studente");
                CommandInputView.CommandListener listener = (cmd, args) -> {
                    var stud = Student.YELLOW;
                    for(var s : Student.values())
                        if(s.name().toLowerCase().startsWith(cmd))
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

    /**
     * Draw islands, clouds and dashboard of the given game update
     * @param game the game update
     */
    private void drawGameView(ReducedGame game) {
        new IslandsLayoutView(game.islands(), game.motherNaturePosition()).draw();
        new DashboardView(game).draw();
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
                1,
                maxSteps
        );

        input.setListener(pos -> client.forwardGameRequest(
                new MoveMotherNatureRequest(pos),
                e -> input.showError(e.getMessage())
        ));
        input.draw();
    }


    /**
     * Check if all students were placed in the students' placement phase
     */
    private int checkIfAllStudentsPlaced(ReducedGame game){
        var count = 0;
        count += studentsPlacedInSchool.getSize();
        for(StudentsContainer sc : studentsPlacedInIslands.values())
            count += sc.getSize();

        var studentsToMove = game.numberOfPlayers() == 2 ?
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
            drawGameView(game);
        }

        return count;
    }

    /**
     * Ask user where to place students
     * @param game the game update
     * TODO: maybe move in a separate view
     */
    private void processPlaceStudents(ReducedGame game) {
        finishedPlacingStudents = false;
        studentsPlacedInSchool = new StudentsContainer();
        studentsPlacedInIslands = new HashMap<>();

        int studentsToMove = game.numberOfPlayers() == 2 ?
                Constants.TwoPlayers.STUDENTS_TO_MOVE : Constants.ThreePlayers.STUDENTS_TO_MOVE;

        var placeStudentsInput = new CommandInputView("Posiziona studenti (0/%d) [esempi 'y 6', 'r sala', 'b 0']".formatted(studentsToMove), false);
        CommandInputView.CommandListener listener = (cmd, args) -> {
            Student stud = null;
            for(var s : Student.values())
                if(s.name().toLowerCase().startsWith(cmd))
                    stud = s;

            if(finishedPlacingStudents){
                placeStudentsInput.showError("Non hai più studenti da piazzare");
                return;
            }

            if(game.getMyPlayer(client.getNickname()).entrance().getCountForStudent(stud) <= 0) {
                placeStudentsInput.showError("Non hai questo studente");
                return;
            }

            if(args.size() != 1) {
                placeStudentsInput.showError("Comando errato (e.g. 'y sala' oppure 'b 10')");
                return;
            }

            var remaining = 0;

            if(args.get(0).equalsIgnoreCase("sala")) { //school

                remaining = placeStudentInSchool(game, stud);

            } else if (Utils.isInteger(args.get(0))) { //island

                var island = Integer.parseInt(args.get(0));

                if(island < 0 || island >= game.islands().size()) {
                    placeStudentsInput.showError("L'isola specificata non esiste");
                    return;
                }

                remaining = placeStudentOnIsland(game, stud, island);
            } else {
                placeStudentsInput.showError("Comando errato (e.g. 'y sala' oppure 'b 10')");
                return;
            }

            if(remaining == 0)
                finishedPlacingStudents = true;
            else
                placeStudentsInput.setMessageAndRedraw("Posiziona studenti (%d/%d) [esempi 'y 6', 'r sala', 'b 0']".formatted(remaining, studentsToMove));
        };

        placeStudentsInput.addCommandListener("y", "Giallo", listener);
        placeStudentsInput.addCommandListener("b", "Blu", listener);
        placeStudentsInput.addCommandListener("g", "Verde", listener);
        placeStudentsInput.addCommandListener("r", "Rosso", listener);
        placeStudentsInput.addCommandListener("p", "Rosa", listener);

        placeStudentsInput.draw();
    }

    /**
     * This method is called when the user wants to place a student on a specific island
     * @param game the game update
     * @param stud the student to place
     * @param island the island to place the student on
     * @return the number of remaining students to place
     */
    private int placeStudentOnIsland(ReducedGame game, Student stud, int island) {
        if(studentsPlacedInIslands.containsKey(island))
            studentsPlacedInIslands.get(island).addStudent(stud);
        else
            studentsPlacedInIslands.put(island, new StudentsContainer().addStudent(stud));

        game.islands().get(island).students().addStudent(stud);
        game.getMyPlayer(client.getNickname()).entrance().removeStudent(stud);

        return checkIfAllStudentsPlaced(game);
    }

    /**
     * This method is called when the user wants to place a student in the school
     * @param game the game update
     * @param stud the student to place
     * @return the number of remaining students to place
     */
    private int placeStudentInSchool(ReducedGame game, Student stud) {
        studentsPlacedInSchool.addStudent(stud);
        game.getMyPlayer(client.getNickname()).entrance().removeStudent(stud);
        game.getMyPlayer(client.getNickname()).school().addStudent(stud);

        return checkIfAllStudentsPlaced(game);
    }

    /**
     * Ask user which cloud to select
     * @param game the game update
     */
    private void processSelectCloud(ReducedGame game) {
        var input = new IntegerInputView("Seleziona una nuvola", 0, game.currentRound().clouds().size() - 1);
        input.setListener(cloud -> client.forwardGameRequest(
                new SelectCloudRequest(game.currentRound().clouds().get(cloud)),
                e -> input.showError(e.getMessage())
        ));
        input.draw();
    }
}
