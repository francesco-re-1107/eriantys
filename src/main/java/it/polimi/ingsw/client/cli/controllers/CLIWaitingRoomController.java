package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;

public class CLIWaitingRoomController implements ScreenController, Client.GameUpdateListener {

    @Override
    public void onShow() {
        Client.getInstance().addGameUpdateListener(this);
    }

    @Override
    public void onHide() {
        Client.getInstance().removeGameUpdateListener(this);
    }


    @Override
    public void onGameUpdate(ReducedGame game) {
        int playersLeft = game.numberOfPlayers() - game.currentNumberOfPlayers();

        Cursor.getInstance().clearScreen();

        if(playersLeft == 1)
            Cursor.getInstance().printCentered("In attesa di 1 giocatore...", 12);
        else
            Cursor.getInstance().printCentered("In attesa di " + playersLeft + " giocatori...", 12);

        Cursor.getInstance().moveToXY(1, Cursor.HEIGHT);

        //if the game is full, go to the game screen
        if(playersLeft == 0)
            Client.getInstance().goToGame();
    }
}
