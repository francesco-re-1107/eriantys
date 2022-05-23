package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.InfoString;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.*;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.requests.MoveMotherNatureRequest;
import it.polimi.ingsw.common.requests.PlaceStudentsRequest;
import it.polimi.ingsw.common.requests.SelectCloudRequest;
import it.polimi.ingsw.server.model.Character;
import it.polimi.ingsw.server.model.RandomizedStudentsContainer;
import it.polimi.ingsw.server.model.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CLIGameController implements ScreenController, Client.GameUpdateListener {
    private String myNickname;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();
        Client.getInstance().addGameUpdateListener(this);
        myNickname = Client.getInstance().getNickname();
    }

    @Override
    public void onHide() {
        Client.getInstance().removeGameUpdateListener(this);
    }

    @Override
    public void onGameUpdate(ReducedGame game) {
        Cursor.getInstance().clearScreen();

        processGameState(game);
    }

    private void processGameState(ReducedGame game) {
        switch (game.currentState()) {
            case STARTED -> {
                new IslandsLayoutView(game.islands(), game.motherNaturePosition()).draw();
                new DashoardView(game).draw();
                new CloudsLayoutView(game.currentRound().clouds()).draw();
                Cursor.getInstance().moveToXY(1, 22);

                var currentPlayer = game.currentRound().currentPlayer();
                if (currentPlayer.equals(myNickname)) {
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
                else if (Client.getInstance().getNickname().equals(winner))
                    new TitleView(TitleView.Title.WIN).draw();
                else
                    new TitleView(TitleView.Title.LOSE, winner + " ha vinto").draw();

            }
        }
    }

    private void processMyTurn(ReducedGame game) {
        var stage = game.currentRound().stage();

        if (stage == Stage.Plan.PLAN) {
            new AssistantCardsView(game.getMyPlayer(myNickname).deck(), null)
                    .draw();
        } else {
            switch ((Stage.Attack) stage) {
                case STARTED -> processPlaceStudents(game);
                case STUDENTS_PLACED -> {
                    var playableCards =
                            game.getOrderedCharacterCards().stream()
                                    .filter(e -> game.getMyPlayer(myNickname).coins() >= e.getKey().getCost(e.getValue()))
                                    .toList();

                    if(playableCards.isEmpty())
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
        //characterCardsView.setListener(r -> {});
        characterCardsView.draw();
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

        input.setListener(pos -> Client.getInstance().forwardGameRequest(
                new MoveMotherNatureRequest(pos),
                e -> input.showError(e.getMessage())
        ));
        input.draw();
    }

    private void processPlaceStudents(ReducedGame game) {
        /*var placeStudents = new CommandInputView();
        placeStudents.addCommandListener("sala", "Sala B (blu in sala)", ((command, args) -> {

        }));*/

        Client.getInstance().forwardGameRequest(
                new PlaceStudentsRequest(
                        new RandomizedStudentsContainer(game.getMyPlayer(myNickname).entrance()).pickManyRandom(3),
                        new HashMap<>()
                ),
                e -> {  }
        );
    }

    private void processSelectCloud(ReducedGame game) {
        var input = new IntegerInputView("Seleziona una nuvola", 0, game.currentRound().clouds().size() - 1);
        input.setListener(cloud -> Client.getInstance().forwardGameRequest(
                new SelectCloudRequest(game.currentRound().clouds().get(cloud)),
                e -> input.showError(e.getMessage())
        ));
        input.draw();
    }
}
