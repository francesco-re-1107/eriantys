package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;

import java.util.Scanner;

public class CLIGameCreationMenuController implements ScreenController {

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();
        System.out.println("This is Game creation menu screen");
        new Thread(() -> {
            new Scanner(System.in).nextLine();
            Client.getInstance().goToGame();
        }).start();
        /*new Scanner(System.in).nextLine();
        Client.getInstance().goToGame();*/
    }

    @Override
    public void onHide() {

    }
}
