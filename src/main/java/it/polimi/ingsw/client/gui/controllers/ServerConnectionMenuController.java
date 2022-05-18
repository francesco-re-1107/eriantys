package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.common.exceptions.NicknameNotValidException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * This class is responsible for controlling the server connection menu screen.
 */
public class ServerConnectionMenuController implements ScreenController {

    @FXML
    public VBox connectionBox;
    @FXML
    public VBox registrationBox;
    @FXML
    public Label connectionError;
    @FXML
    public Label registrationError;
    @FXML
    private TextField hostTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private Button connectButton;
    @FXML
    public Button registerButton;

    public void initialize() {
        //set nickname text field max length
        nicknameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (nicknameTextField.getText().length() > Constants.MAX_NICKNAME_LENGTH) {
                var s = nicknameTextField.getText().substring(0, Constants.MAX_NICKNAME_LENGTH);
                nicknameTextField.setText(s);
            }
        });
    }

    @Override
    public void onShow() {
        connectionBox.requestFocus();
        connectionError.setVisible(false);
        registrationError.setVisible(false);
        showConnectionBox();
    }

    @Override
    public void onHide() {
        //nothing to do
    }

    /**
     * Shows the connection box and hides the registration box.
     */
    private void showConnectionBox() {
        registrationBox.setVisible(false);
        registrationBox.setManaged(false);
        connectionBox.setVisible(true);
        connectionBox.setManaged(true);
    }

    /**
     * Shows the registration box and hides the connection box.
     */
    private void showRegistrationBox() {
        connectionBox.setVisible(false);
        connectionBox.setManaged(false);
        registrationBox.setVisible(true);
        registrationBox.setManaged(true);

        connectButton.setDisable(false);
        registerButton.setDisable(false);

        registrationBox.requestFocus();
    }

    /**
     * Callback for the connect button.
     */
    public void connect() {
        connectButton.setDisable(true);

        Client.getInstance()
                .connect(
                        hostTextField.getText(),
                        portTextField.getText(),
                        () -> {
                            showRegistrationBox();
                            connectButton.setDisable(false);
                        },
                        e -> {
                            showConnectionError(e.getMessage());
                            connectButton.setDisable(false);
                        }
                );
    }

    /**
     * Callback for the register button.
     */
    public void register() {
        registerButton.setDisable(true);

        Client.getInstance()
                .registerNickname(nicknameTextField.getText(), e -> {
                    if(e instanceof DuplicatedNicknameException) {
                        showRegistrationError("Nickname già presente sul server");
                    } else if(e instanceof NicknameNotValidException){
                        showRegistrationError("Nickname non valido");
                    }
                    registerButton.setDisable(false);
                });
    }

    /**
     * Shows the connection error message.
     * @param message
     */
    private void showConnectionError(String message) {
        Platform.runLater(() -> {
            connectionError.setVisible(true);
            connectionError.setText(message);
        });
    }

    /**
     * Shows the registration error message.
     * @param message
     */
    private void showRegistrationError(String message) {
        Platform.runLater(() -> {
            registrationError.setVisible(true);
            registrationError.setText(message);
        });
    }

    public void onLeavePressed() {
        Client.getInstance().exitApp();
    }
}
