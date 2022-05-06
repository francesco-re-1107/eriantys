package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WaitingRoomController {
    @FXML
    Button leaveButton;

    @FXML
    private void onLeavePressed(){
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    public void leaveGame() {
        NavigationManager.getInstance().navigateTo(NavigationManager.Screen.MAIN_MENU, false);
    }
}
