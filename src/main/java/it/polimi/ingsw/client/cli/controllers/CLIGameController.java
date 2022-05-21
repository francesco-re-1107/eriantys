package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.DashoardView;
import it.polimi.ingsw.client.cli.views.IslandView;
import it.polimi.ingsw.client.cli.views.IslandsLayoutView;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;

public class CLIGameController implements ScreenController, Client.GameUpdateListener {

    private IslandView iv;

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

        if(game.currentRound() != null) {
            new IslandsLayoutView(game.islands(), game.motherNaturePosition()).draw();
            new DashoardView(game).draw();
        }
    }
}
