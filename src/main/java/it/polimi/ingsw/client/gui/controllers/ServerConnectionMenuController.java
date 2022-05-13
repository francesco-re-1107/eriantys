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

        var host = hostTextField.getText();

        if(host.isBlank())
            host = Constants.DEFAULT_HOSTNAME;

        var port = Constants.DEFAULT_SERVER_PORT;

        //if a port is defined try to parse it
        if(!portTextField.getText().isBlank())
            try{
                port = Integer.parseInt(portTextField.getText());
            }catch (NumberFormatException e){
                showConnectionError("Inserisci una porta valida");
                return;
            }

        Client.getInstance()
                .connect(
                        host,
                        port,
                        () -> {
                                showRegistrationBox();
                                connectButton.setDisable(false);
                            },
                        e -> {
                            showConnectionError("Impossibile connettersi al server");
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
        //nothing to do
        hostTextField.clear();
        portTextField.clear();
        nicknameTextField.clear();

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

        registrationBox.requestFocus();
    }

    @Override
    public void onHide() {
        //nothing to do
    }

    public void register() {
        registerButton.setDisable(true);
        var nickname = nicknameTextField.getText();

        if(nickname.isBlank())
            nickname = Utils.generateRandomNickname();

        Utils.LOGGER.info("Trying to register with nickname: " + nickname);

        Client.getInstance()
                .registerNickname(nickname, e -> {
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
