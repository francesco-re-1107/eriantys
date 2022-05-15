package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WaitingRoomController implements ScreenController, Client.GameUpdateListener {
    @FXML
    public Label waitingLabel;
    @FXML
    public Label nicknameLabel;

    @FXML
    Button leaveButton;

    @FXML
    private void onLeavePressed(){
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    public void leaveGame() {
        Client.getInstance().leaveGame();
    }

    @Override
    public void onCreate() {
        //nothing to do
    }

    @Override
    public void onShow() {
        Client.getInstance().addGameUpdateListener(this);
        nicknameLabel.setText("Connesso come " + Client.getInstance().getNickname());
    }

    @Override
    public void onHide() {
        Client.getInstance().removeGameUpdateListener(this);
    }

    @Override
    public void onGameUpdate(ReducedGame game) {
        int playersLeft = game.numberOfPlayers() - game.currentNumberOfPlayers();

        Platform.runLater(
                () -> waitingLabel.setText("In attesa di altri " + playersLeft + " giocatori...")
        );

        if(playersLeft == 0)
            Client.getInstance().goToGame();
    }
}
