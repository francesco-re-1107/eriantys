package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.InfoString;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.*;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.requests.MoveMotherNatureRequest;
import it.polimi.ingsw.common.requests.PlaceStudentsRequest;
import it.polimi.ingsw.common.requests.SelectCloudRequest;
import it.polimi.ingsw.server.model.RandomizedStudentsContainer;
import it.polimi.ingsw.server.model.Stage;

import java.util.HashMap;

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
                case STUDENTS_PLACED -> processMoveMotherNature(game); //TODO: check if user wants to play a character card
                case CARD_PLAYED -> processMoveMotherNature(game);
                case MOTHER_NATURE_MOVED -> processSelectCloud(game);
            }
        }
    }

    private void processMoveMotherNature(ReducedGame game) {
        var input = new SimpleInputView("Di quanti passi vuoi far muovere madre natura (max " + game.calculateMaxMotherNatureSteps() + " passi)?");
        input.setListener(pos -> {
            try {
                var choice = Integer.parseInt(pos);
                if(choice <= 0 || choice > game.calculateMaxMotherNatureSteps()) {
                    input.showError("Isola non valida");
                    return;
                }
                Client.getInstance().forwardGameRequest(
                        new MoveMotherNatureRequest(choice),
                        e -> input.showError(e.getMessage())
                );
            } catch (NumberFormatException e) {
                input.showError("Formato non valido");
            }
        });
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
        var input = new SimpleInputView("Seleziona una nuvola");
        input.setListener(cloud -> {
            try {
                var choice = Integer.parseInt(cloud);
                if(choice < 0 || choice >= game.currentRound().clouds().size()) {
                    input.showError("Nuvola non valida");
                    return;
                }
                Client.getInstance().forwardGameRequest(
                        new SelectCloudRequest(game.currentRound().clouds().get(choice)),
                        e -> input.showError(e.getMessage())
                );
            } catch (NumberFormatException e) {
                input.showError("Formato non valido");
            }
        });
        input.draw();
    }
}
