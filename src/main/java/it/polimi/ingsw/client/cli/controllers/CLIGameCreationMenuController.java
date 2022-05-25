package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.BooleanInputView;
import it.polimi.ingsw.client.cli.views.IntegerInputView;
import it.polimi.ingsw.client.cli.views.TitleView;

/**
 * This class is responsible for controlling the game creation menu screen of the CLI.
 */
public class CLIGameCreationMenuController implements ScreenController {

    /**
     * Input view for the number of players.
     */
    private IntegerInputView numberOfPlayersView;

    /**
     * Input view for the expert mode.
     */
    private BooleanInputView expertModeView;

    /**
     * Number of players chosen by the user.
     */
    private int numberOfPlayers;

    /**
     * Expert mode chosen by the user.
     */
    private boolean expertMode;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();

        new TitleView(TitleView.Title.ERIANTYS, "Connesso come " + Client.getInstance().getNickname())
                .draw();

        numberOfPlayersView = new IntegerInputView("Inserisci numero di giocatori [2-3]: ", 2, 3);
        numberOfPlayersView.setListener(n -> {
            numberOfPlayers = n;
            askForExpertMode();
        });

        expertModeView = new BooleanInputView("Inserisci modalità esperti [s/n]: ");
        expertModeView.setListener(e -> {
            expertMode = e;
            sendCreationRequest();
        });
        numberOfPlayersView.draw();
    }

    /**
     * Sends the game creation request to the server.
     */
    private void sendCreationRequest() {
        Client.getInstance().createGame(
                numberOfPlayers,
                expertMode,
                e -> numberOfPlayersView.draw()
        );
    }

    /**
     * Asks user for expert mode.
     */
    private void askForExpertMode() {
        expertModeView.draw();

    }

    @Override
    public void onHide() {
        //nothing to do
    }
}
