package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.SimpleInputView;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;

/**
 * This class is responsible for controlling the waiting room screen of the CLI.
 */
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
            Cursor.getInstance().printCentered("In attesa di 1 giocatore...", 10);
        else
            Cursor.getInstance().printCentered("In attesa di " + playersLeft + " giocatori...", 10);

        var input = new SimpleInputView("Premi invio per abbandonare la partita");
        input.setListener(r -> Client.getInstance().leaveGame());
        input.draw();

        //if the game is full, go to the game screen
        if(playersLeft == 0)
            Client.getInstance().goToGame();
    }
}
