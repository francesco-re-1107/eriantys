package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    Button leaveButton;

    @FXML
    private void onLeavePressed(){
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    public void createGame(MouseEvent mouseEvent) {
        NavigationManager.getInstance().navigateTo(NavigationManager.Screen.GAME_CREATION_MENU);
    }

    public void joinGame(MouseEvent mouseEvent) {
        NavigationManager.getInstance().navigateTo(NavigationManager.Screen.GAME_JOINING_MENU);
    }
}
