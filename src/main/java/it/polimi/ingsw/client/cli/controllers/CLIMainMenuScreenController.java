package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.commands.ExitCommand;
import it.polimi.ingsw.client.cli.views.CommandInputView;

public class CLIMainMenuScreenController implements ScreenController {

    private CommandInputView commandInput;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();
        System.out.println("This is main menu");
        commandInput = new CommandInputView();
        commandInput.setCommandListener(c -> {
            if(c instanceof ExitCommand) {
                Client.getInstance().exitApp();
            }
        });
        commandInput.draw();
    }

    @Override
    public void onHide() {

    }
}
