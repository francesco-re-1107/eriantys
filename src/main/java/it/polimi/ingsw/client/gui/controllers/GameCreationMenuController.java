package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import javafx.application.Platform;
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
    public Label creationError;
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
        Client.getInstance().createGame(numberOfPlayers, expertMode, e -> {
            showCreationError(e.getMessage());
        });
    }

    private void showCreationError(String message) {
        Platform.runLater(() -> {
            creationError.setVisible(true);
            creationError.setText(message);
        });
    }

    @Override
    public void onShow() {
        nicknameLabel.setText("Connesso come " + Client.getInstance().getNickname());
        creationError.setVisible(false);
        playersGroup.selectToggle(playersGroup.getToggles().get(0));
        expertGroup.selectToggle(expertGroup.getToggles().get(0));
        numberOfPlayers = 2;
        expertMode = false;
    }

    @Override
    public void onHide() {
        //nothing to do
    }
}
