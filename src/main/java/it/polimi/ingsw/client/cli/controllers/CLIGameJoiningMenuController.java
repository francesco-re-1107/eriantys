package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.ListView;
import it.polimi.ingsw.common.reducedmodel.GameListItem;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static org.fusesource.jansi.Ansi.ansi;

public class CLIGameJoiningMenuController implements ScreenController {

    private ListView<GameListItem> gamesListView;

    private Timer refreshTimer;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();

        gamesListView = new ListView<>(new ArrayList<>(), item -> {
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
        }, "Partite disponibili", "Seleziona una partita", "Nessuna partita disponibile");

        gamesListView.setListener((item, index) -> joinGame(item));

        //start list refresh when the screen is displayed
        startRefreshTimer();
    }

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
                        list -> gamesListView.setListItems(list),
                        e -> Utils.LOGGER.info(e.getMessage())
                );
            }
        }, 0, Constants.GAMES_LIST_REFRESH_INTERVAL);
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
