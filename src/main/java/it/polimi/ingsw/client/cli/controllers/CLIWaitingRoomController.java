package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;

public class CLIWaitingRoomController implements ScreenController {

    @Override
    public void onShow() {
        Cursor.getInstance().eraseScreen();
        System.out.println("This is waiting room screen");
    }

    @Override
    public void onHide() {

    }
}
