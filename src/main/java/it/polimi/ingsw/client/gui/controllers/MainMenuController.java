package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenuController implements ScreenController {

    @FXML
    public Label nicknameLabel;

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
    public void onShow() {
        nicknameLabel.setText("Connesso come " + Client.getInstance().getNickname());
    }

    @Override
    public void onHide() {
    }
}
