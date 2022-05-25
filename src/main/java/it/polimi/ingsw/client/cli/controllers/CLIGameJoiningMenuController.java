package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.ListView;
import it.polimi.ingsw.common.reducedmodel.GameListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * This class is responsible for controlling the game joining menu of the CLI.
 */
public class CLIGameJoiningMenuController implements ScreenController {

    /**
     * ListView for the games.
     */
    private ListView<GameListItem> gamesListView;

    /**
     * Timer for refreshing the list of games.
     */
    private Timer refreshTimer;

    /**
     * Current list of games to display.
     */
    private List<GameListItem> currentGamesList;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();

        currentGamesList = new ArrayList<>();
        gamesListView = new ListView<>(
                currentGamesList,
                item -> {
                    if(item.expertMode()){
                        return ansi()
                                .a("GIOCATORI: ")
                                .a(item.currentNumberOfPlayers() + "/" + item.numberOfPlayers())
                                .a("  ")
                                .fgBrightRed().a("ESPERTI ")
                                .reset();
                    } else {
                        return ansi()
                                .a("GIOCATORI: ")
                                .a(item.currentNumberOfPlayers() + "/" + item.numberOfPlayers()).reset()
                                .a("  ")
                                .fgBrightYellow().a("SEMPLICE")
                                .reset();
                    }
                },
                "Partite disponibili",
                "Seleziona una partita",
                "Nessuna partita disponibile",
                true,
                2
        );

        gamesListView.setOnSelectionListener((item, index) -> joinGame(item));
        gamesListView.draw();

        //start list refresh when the screen is displayed
        startRefreshTimer();
    }

    /**
     * Called when the user selects a game from the list.
     * @param item the selected game item
     */
    private void joinGame(GameListItem item) {
        Client.getInstance().joinGame(item.uuid(), e -> gamesListView.showError("Impossibile unirsi alla partita"));
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
                        CLIGameJoiningMenuController.this::updateGamesList,
                        e -> Utils.LOGGER.info(e.getMessage())
                );
            }
        }, 0, Constants.GAMES_LIST_REFRESH_INTERVAL);
    }

    private void updateGamesList(List<GameListItem> list) {
        if(Utils.isSameList(list, currentGamesList)) return;

        currentGamesList = list;
        gamesListView.setListItems(list);
    }

    /**
     * Stops the refresh timer.
     */
    private void stopRefreshTimer() {
        refreshTimer.cancel();
        refreshTimer.purge();
    }

    @Override
    public void onHide() {
        stopRefreshTimer();
    }
}
