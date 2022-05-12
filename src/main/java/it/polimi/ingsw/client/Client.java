package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientServerCommunicator.CommunicatorListener;
import it.polimi.ingsw.client.gui.GUINavigationManager;
import it.polimi.ingsw.common.reducedmodel.GameListItem;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.requests.*;
import it.polimi.ingsw.common.responses.Update;
import it.polimi.ingsw.common.responses.replies.GamesListReply;
import it.polimi.ingsw.common.responses.updates.GameUpdate;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Client implements CommunicatorListener {

    private ClientServerCommunicator communicator;

    private final NavigationManager navigationManager;

    private static Client instance;
    private List<GameUpdateListener> gameUpdateListeners;
    private ReducedGame lastGameUpdate;

    /**
     * Instantiates a new CLI Client.
     */
    private Client() {
        gameUpdateListeners = new ArrayList<>();

        //navigationManager = new CLINavigationManager();
        navigationManager = null;
    }

    /**
     * Instantiates a new GUI Client.
     */
    private Client(Stage stage) {
        gameUpdateListeners = new ArrayList<>();
        navigationManager = new GUINavigationManager(stage);

        navigationManager.navigateTo(Screen.MAIN_MENU);
    }

    public static void init() {
        instance = new Client();
    }

    public static void init(Stage stage) {
        instance = new Client(stage);
    }

    public static Client getInstance() {
        if (instance == null)
            throw new RuntimeException("Client not initialized");

        return instance;
    }

    @Override
    public void onUpdate(Update u) {
        if (u instanceof GameUpdate gu) {
            lastGameUpdate = gu.getGame();
            gameUpdateListeners.forEach(l -> l.onGameUpdate(gu.getGame()));
        }

    }

    @Override
    public void onDisconnect() {
        tryReconnection();

        //navigationManager.navigateTo(Screen.SERVER_CONNECTION_MENU);
    }

    private void tryReconnection() {
        //...
    }

    /**
     * Connect to the server at the given host and port, and call the successListener if the connection is successful, or
     * the errorListener if the connection fails
     *
     * @param host            the hostname of the server to connect to.
     * @param port            the port to connect to.
     * @param successListener a Runnable that will be executed if the connection is successful.
     * @param errorListener   an error listener that will be called if an error occurs.
     */
    public void connect(String host, int port, Runnable successListener, Consumer<Throwable> errorListener) {
        try {
            communicator = new ClientServerCommunicator(new Socket(host, port), this);
            communicator.startListening();

        } catch (IOException e) {
            errorListener.accept(e);
        }
    }


    public void registerNickname(String nickname, Consumer<Throwable> errorListener) {
        communicator.send(new RegisterNicknameRequest(nickname),
                r -> {
                    if (r.isSuccessful())
                        navigationManager.navigateTo(Screen.MAIN_MENU);
                    else
                        errorListener.accept(r.getThrowable());
                },
                errorListener::accept
        );
    }

    public void goToGameCreationMenu() {
        navigationManager.navigateTo(Screen.GAME_CREATION_MENU);
    }

    public void goToGameJoiningMenu() {
        navigationManager.navigateTo(Screen.GAME_JOINING_MENU);
    }

    public void createGame(int numberOfPlayers, boolean expertMode, Consumer<Throwable> errorListener) {
        communicator.send(
                new CreateGameRequest(numberOfPlayers, expertMode),
                r -> {
                    if (r.isSuccessful())
                        navigationManager.navigateTo(Screen.WAITING_ROOM);
                    else
                        errorListener.accept(r.getThrowable());
                },
                errorListener::accept
        );
    }

    public void exitApp() {
        navigationManager.exitApp();
    }

    public void goBack() {
        navigationManager.goBack();
    }

    public void joinGame(UUID uuid, Consumer<Throwable> errorListener) {
        communicator.send(
                new JoinGameRequest(uuid),
                r -> {
                    if (r.isSuccessful())
                        navigationManager.navigateTo(Screen.WAITING_ROOM);
                    else
                        errorListener.accept(r.getThrowable());
                },
                errorListener::accept
        );
    }

    public void leaveGame(Consumer<Throwable> errorListener) {
        communicator.send(
                new LeaveGameRequest(),
                r -> {
                    if (r.isSuccessful()) {
                        navigationManager.navigateTo(Screen.MAIN_MENU);
                        lastGameUpdate = null;
                    }else
                        errorListener.accept(r.getThrowable());
                },
                errorListener::accept
        );
    }

    public void addGameUpdateListener(GameUpdateListener listener) {
        gameUpdateListeners.add(listener);
        if(lastGameUpdate != null)
            listener.onGameUpdate(lastGameUpdate);
    }

    public void removeGameUpdateListener(GameUpdateListener listener) {
        gameUpdateListeners.remove(listener);
    }

    public void goToGame() {
        navigationManager.navigateTo(Screen.GAME);
    }

    public void getGameList(Consumer<List<GameListItem>> listener, Consumer<Throwable> errorListener) {
        communicator.send(
                new ListGamesRequest(),
                r -> {
                    if (r.isSuccessful())
                        listener.accept(((GamesListReply)r).getGamesList());
                    else
                        errorListener.accept(r.getThrowable());
                },
                errorListener::accept
        );
    }

    public interface GameUpdateListener {
        void onGameUpdate(ReducedGame game);
    }
}
