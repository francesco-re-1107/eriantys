package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.CommandInputView;
import it.polimi.ingsw.client.cli.views.TitleView;

/**
 * This class is responsible for controlling the main menu screen of the CLI.
 */
public class CLIMainMenuScreenController implements ScreenController {

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();

        new TitleView(TitleView.Title.ERIANTYS, "Connesso come " + Client.getInstance().getNickname())
                .draw();

        var commandInputView = new CommandInputView("Scegli opzione");
        commandInputView.addCommandListener(
                "0",
                "CREA PARTITA",
                (c, args) -> Client.getInstance().goToGameCreationMenu()
        );
        commandInputView.addCommandListener(
                "1",
                "UNISCITI A PARTITA",
                (c, args) -> Client.getInstance().goToGameJoiningMenu()
        );
        commandInputView.draw();
    }

    @Override
    public void onHide() {
        //nothing to do
    }
}
