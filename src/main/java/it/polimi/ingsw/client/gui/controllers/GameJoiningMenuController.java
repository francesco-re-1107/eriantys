package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.NavigationManager;
import it.polimi.ingsw.client.gui.customviews.GameListItemView;
import it.polimi.ingsw.common.reducedmodel.GameListItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.UUID;

public class GameJoiningMenuController {

    @FXML
    public Button leaveButton;
    @FXML
    public VBox gamesList;

    @FXML
    public void initialize() {
        for (int i = 0; i < 10; i++) {
            var item = new GameListItemView(new GameListItem(UUID.randomUUID(), 3, 1, true));
            item.setOnJoinButtonClicked(e -> {

            });
            gamesList.getChildren().add(item);
        }
    }

    public void onLeavePressed(ActionEvent actionEvent) {
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    public void goBack(MouseEvent mouseEvent) {
        NavigationManager.getInstance().goBack();
    }
}
