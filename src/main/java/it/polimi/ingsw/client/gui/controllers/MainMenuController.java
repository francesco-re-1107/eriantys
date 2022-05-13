package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import javafx.fxml.FXML;

public class MainMenuController implements ScreenController {

    @FXML
    private void onLeavePressed(){
        Client.getInstance().exitApp();
    }

    public void createGame() {
        Client.getInstance().goToGameCreationMenu();
    }

    public void joinGame() {
        Client.getInstance().goToGameJoiningMenu();
    }

    @Override
    public void onCreate() {
        //nothing to do
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
    }
}
