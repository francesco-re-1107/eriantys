package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.gui.customviews.GameListItemView;
import it.polimi.ingsw.common.reducedmodel.GameListItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameJoiningMenuController implements ScreenController {
    @FXML
    public VBox gamesList;
    private Timer refreshTimer;

    private List<GameListItem> currentGamesList = new ArrayList<>();

    public void setGamesList(List<GameListItem> list) {
        if(Utils.isSameList(list, currentGamesList)) return;

        currentGamesList = list;
        gamesList.getChildren().clear();

        if(list.isEmpty()) {
            gamesList.getChildren().add(new Label("Nessuna partita in corso"));
        } else {
            list.forEach(i -> {
                var item = new GameListItemView(i);
                item.setOnJoinButtonClicked(e -> joinGame(i));
                gamesList.getChildren().add(item);
            });
        }
    }

    private void joinGame(GameListItem item) {
        Utils.LOGGER.info("Joining game " + item.uuid());

        Client.getInstance().joinGame(item.uuid(), e -> {
            Utils.LOGGER.info(e.getMessage());
            //show error
        });
    }

    public void onLeavePressed() {
        Client.getInstance().exitApp();
    }

    public void goBack() {
        Client.getInstance().goBack();
    }

    @Override
    public void onCreate() {
        //nothing to do
    }

    @Override
    public void onShow() {
        startRefreshTimer();
    }

    /**
     * Starts the refresh timer.
     * The timer will refresh the list of games every 5 seconds.
     */
    private void startRefreshTimer() {
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Client.getInstance().getGameList(
                        list -> Platform.runLater(() -> setGamesList(list)),
                        e -> {
                            Utils.LOGGER.info(e.getMessage());
                            //show error...
                        });
            }
        }, 0, Constants.GAMES_LIST_REFRESH_INTERVAL);
    }

    @Override
    public void onHide() {
        stopRefreshTimer();
    }

    /**
     * Stops the refresh timer.
     */
    private void stopRefreshTimer() {
        refreshTimer.cancel();
        refreshTimer.purge();
    }
}
