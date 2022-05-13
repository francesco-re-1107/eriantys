package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class GameCreationMenuController implements ScreenController {
    public ToggleGroup playersGroup;
    public ToggleGroup expertGroup;
    @FXML
    public Label nicknameLabel;

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
        Client.getInstance().exitApp();
    }

    public void goBack() {
        Client.getInstance().goBack();
    }

    public void createGame() {
        Utils.LOGGER.info("Creating game with " + numberOfPlayers + " players and " + (expertMode ? "expert" : "simple") + " mode");
        Client.getInstance().createGame(numberOfPlayers, expertMode, e -> {
            Utils.LOGGER.info(e.getMessage());
            //show error message
        });
    }

    @Override
    public void onCreate() {
        //nothing to do
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
