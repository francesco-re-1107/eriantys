package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.gui.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class GameCreationMenuController {
    public ToggleGroup playersGroup;
    public ToggleGroup expertGroup;

    @FXML
    Button leaveButton;

    private int numberOfPlayers = 2;

    private boolean expertMode = false;

    public void initialize() {
        playersGroup.selectToggle(playersGroup.getToggles().get(0));
        expertGroup.selectToggle(expertGroup.getToggles().get(0));


        playersGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton rb = (RadioButton) newValue;
            numberOfPlayers = "2".equals(rb.getText()) ? 2 : 3;
        });

        expertGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton rb = (RadioButton) newValue;
            expertMode = "SI".equals(rb.getText());
        });
    }

    @FXML
    private void onLeavePressed(){
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    public void goBack() {
        NavigationManager.getInstance().goBack();
    }

    public void createGame() {
        Utils.LOGGER.info("Creating game with " + numberOfPlayers + " players and " + (expertMode ? "expert" : "simple") + " mode");
        NavigationManager.getInstance().navigateTo(NavigationManager.Screen.GAME);
    }
}
