package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;
import it.polimi.ingsw.common.exceptions.NicknameNotValidException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ServerConnectionMenuController implements ScreenController {

    @FXML
    public VBox connectionBox;
    @FXML
    public VBox registrationBox;
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

    private void showConnectionError(String message) {
        Utils.LOGGER.info(message);
    }

    @Override
    public void onCreate() {
        //nothing to do
    }

    @Override
    public void onShow() {
        connectionBox.requestFocus();
        showConnectionBox();
    }

    private void showConnectionBox() {
        registrationBox.setVisible(false);
        registrationBox.setManaged(false);
        connectionBox.setVisible(true);
        connectionBox.setManaged(true);
    }

    private void showRegistrationBox() {
        connectionBox.setVisible(false);
        connectionBox.setManaged(false);
        registrationBox.setVisible(true);
        registrationBox.setManaged(true);

        connectButton.setDisable(false);
        registerButton.setDisable(false);

        registrationBox.requestFocus();
    }

    @Override
    public void onHide() {
        //nothing to do
    }

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

    private void showRegistrationError(String message) {
        Utils.LOGGER.info(message);
    }

    public void onLeavePressed() {
        Client.getInstance().exitApp();
    }
}
