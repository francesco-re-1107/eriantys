package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This class is responsible for controlling the main menu screen.
 */
public class GUIMainMenuController implements ScreenController {

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
        //nothing to do
    }
}
