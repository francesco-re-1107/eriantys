package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.BooleanInputView;
import it.polimi.ingsw.client.cli.views.IntegerInputView;
import it.polimi.ingsw.client.cli.views.TitleView;

public class CLIGameCreationMenuController implements ScreenController {

    private IntegerInputView numberOfPlayersView;

    private BooleanInputView expertModeView;

    private int numberOfPlayers;

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

    private void sendCreationRequest() {
        Client.getInstance().createGame(
                numberOfPlayers,
                expertMode,
                e -> numberOfPlayersView.draw()
        );
    }

    private void askForExpertMode() {
        expertModeView.draw();

    }

    @Override
    public void onHide() {
        //nothing to do
    }
}
