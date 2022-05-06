package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.gui.NavigationManager;
import it.polimi.ingsw.client.gui.customviews.GameListItemView;
import it.polimi.ingsw.common.reducedmodel.GameListItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.UUID;

public class GameJoiningMenuController {

    @FXML
    public Button leaveButton;
    @FXML
    public VBox gamesList;

    @FXML
    public void initialize() {
        var i = new GameListItem(UUID.randomUUID(), 3, 1, true);
        setGamesList(List.of(i, i, i, i, i));

        setupRefreshTimer();
    }

    private void setupRefreshTimer() {
        //setup timer every 5 seconds
    }

    public void setGamesList(List<GameListItem> list) {
        gamesList.getChildren().clear();
        list.forEach(i -> {
            var item = new GameListItemView(i);
            item.setOnJoinButtonClicked(e -> joinGame(i));
            gamesList.getChildren().add(item);
        });
    }

    private void joinGame(GameListItem item) {
        Utils.LOGGER.info("Joining game " + item.uuid());
        //...
        NavigationManager.getInstance().navigateTo(NavigationManager.Screen.WAITING_ROOM);
    }

    public void onLeavePressed(ActionEvent actionEvent) {
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    public void goBack(MouseEvent mouseEvent) {
        NavigationManager.getInstance().goBack();
    }
}
