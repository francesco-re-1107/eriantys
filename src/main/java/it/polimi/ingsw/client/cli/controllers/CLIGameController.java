package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.DashoardView;
import it.polimi.ingsw.client.cli.views.IslandsLayoutView;
import it.polimi.ingsw.client.cli.views.TitleView;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.model.Stage;

public class CLIGameController implements ScreenController, Client.GameUpdateListener {

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();
        Client.getInstance().addGameUpdateListener(this);
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
                if (game.currentRound().stage() == Stage.Plan.PLAN) {
                    processPlanStage(game);
                }
                else {
                    new IslandsLayoutView(game.islands(), game.motherNaturePosition()).draw();
                    new DashoardView(game).draw();
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
                if(winner == null)
                    new TitleView(TitleView.Title.TIE).draw();
                else if(Client.getInstance().getNickname().equals(winner))
                    new TitleView(TitleView.Title.WIN).draw();
                else
                    new TitleView(TitleView.Title.LOSE, winner + " ha vinto").draw();

            }
        }
    }

    private void processPlanStage(ReducedGame game){
        final String playerNickname = Client.getInstance().getNickname();
        if (game.currentRound().currentPlayer() == playerNickname) {
            // todo
        }
        else{
            // todo
        }
    }
}
