package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.SimpleInputView;
import it.polimi.ingsw.client.cli.views.TitleView;

public class CLIGameCreationMenuController implements ScreenController {

    private SimpleInputView numberOfPlayersView;

    private SimpleInputView expertModeView;

    private int numberOfPlayers;

    private boolean expertMode;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();

        new TitleView(TitleView.Title.ERIANTYS, "Connesso come " + Client.getInstance().getNickname())
                .draw();

        numberOfPlayersView = new SimpleInputView("Inserisci numero di giocatori (2-3): ");
        numberOfPlayersView.setListener(n -> {
            try {
                int numberOfPlayers = Integer.parseInt(n);
                if (numberOfPlayers < 2 || numberOfPlayers > 3) {
                    numberOfPlayersView.showError("Numero di giocatori non valido");
                } else {
                    this.numberOfPlayers = numberOfPlayers;
                    askForExpertMode();
                }
            } catch (NumberFormatException e) {
                numberOfPlayersView.showError("Formato non valido");
            }
        });
        expertModeView = new SimpleInputView("Inserisci modalità esperti (s/n): ");
        expertModeView.setListener(e -> {
            var improved = e.trim().toLowerCase();
            if (improved.equals("s")) {
                expertMode = true;
            } else if (improved.equals("n")) {
                expertMode = false;
            } else {
                expertModeView.showError("Formato non valido");
                return;
            }

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

    }
}
