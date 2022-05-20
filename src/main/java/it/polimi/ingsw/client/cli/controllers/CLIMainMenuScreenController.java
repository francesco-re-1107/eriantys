package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.CommandInputView;
import it.polimi.ingsw.client.cli.views.TitleView;

public class CLIMainMenuScreenController implements ScreenController {

    private TitleView titleView;

    private CommandInputView commandInputView;

    @Override
    public void onShow() {
        Cursor.getInstance().eraseScreen();

        titleView = new TitleView(TitleView.Title.ERIANTYS,
                "Connesso come " + Client.getInstance().getNickname());
        titleView.draw();

        commandInputView = new CommandInputView();
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
        System.out.println("CLIMainMenuScreenController.onHide()");
    }
}
