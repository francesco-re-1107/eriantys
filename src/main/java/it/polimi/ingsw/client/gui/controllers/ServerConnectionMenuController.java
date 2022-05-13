package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ServerConnectionMenuController implements ScreenController {

    @FXML
    private TextField hostTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private Button connectButton;

    public void connect() {
        var host = "localhost"; //hostTextField.getText();
        var port = 6001;
        /*try{
            port = Integer.parseInt(portTextField.getText());
        }catch (NumberFormatException e){
            Utils.LOGGER.info("Port is not a number");
            //show error...
        }*/

        Client.getInstance()
                .connect(
                        host,
                        port,
                        () -> {
                            Utils.LOGGER.info("Connected to server");
                        },
                        e -> {
                            Utils.LOGGER.info(e.getMessage());
                            //show error...
                        });
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
    }

    @Override
    public void onHide() {
        //nothing to do
    }

    public void register() {
        var nickname = nicknameTextField.getText();

        Client.getInstance()
                .registerNickname(nickname, e -> {
                    Utils.LOGGER.info(e.getMessage());
                    //show error...
                });
    }
}
