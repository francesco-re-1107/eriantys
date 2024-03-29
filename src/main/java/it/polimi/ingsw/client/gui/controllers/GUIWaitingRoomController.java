package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * This class is responsible for controlling the waiting room screen.
 */
public class GUIWaitingRoomController implements ScreenController, Client.GameUpdateListener {
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

    /**
     * This method is called when the user presses the leave game button.
     */
    public void leaveGame() {
        Client.getInstance().leaveGame();
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
                () -> {
                    if(playersLeft == 1)
                        waitingLabel.setText("In attesa di 1 giocatore...");
                    else
                        waitingLabel.setText("In attesa di " + playersLeft + " giocatori...");
                }
        );

        //if the game is full, go to the game screen
        if(playersLeft == 0)
            Client.getInstance().goToGame();
    }
}
