package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.AssistantCardsView;
import it.polimi.ingsw.client.cli.views.CloudsLayoutView;
import it.polimi.ingsw.client.cli.views.DashoardView;
import it.polimi.ingsw.client.cli.views.IslandsLayoutView;
import it.polimi.ingsw.client.cli.views.ListView;
import it.polimi.ingsw.client.cli.views.TitleView;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.model.Stage;

public class CLIGameController implements ScreenController, Client.GameUpdateListener {
    private String playerNickname;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();
        Client.getInstance().addGameUpdateListener(this);
        playerNickname = Client.getInstance().getNickname();
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

                if (game.currentRound().currentPlayer().equals(playerNickname)) {
                    processMyTurn(game);
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
            new AssistantCardsView(game.findMyPlayer(playerNickname).deck(), null)
                    .draw();
        } else {
            switch ((Stage.Attack) stage) {
                case CARD_PLAYED:
                    break;
                case MOTHER_NATURE_MOVED:
                    break;
                case SELECTED_CLOUD:
                    break;
                case STARTED:
                    break;
                case STUDENTS_PLACED:
                    break;
                default:
                    break;
            }
        }
    }
}
