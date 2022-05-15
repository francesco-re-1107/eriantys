package it.polimi.ingsw.client;

import it.polimi.ingsw.Constants;
import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.ClientServerCommunicator.CommunicatorListener;
import it.polimi.ingsw.client.gui.GUINavigationManager;
import it.polimi.ingsw.common.reducedmodel.GameListItem;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.requests.*;
import it.polimi.ingsw.common.responses.Update;
import it.polimi.ingsw.common.responses.replies.GamesListReply;
import it.polimi.ingsw.common.responses.updates.GameUpdate;
import it.polimi.ingsw.server.model.Game;
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

    private String nickname;

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

        navigationManager.navigateTo(Screen.SERVER_CONNECTION_MENU);
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
            new ArrayList<>(gameUpdateListeners)
                    .forEach(l -> l.onGameUpdate(gu.getGame()));

            //if there was a game played, go to the game screen
            var currScreen = navigationManager.getCurrentScreen();
            if (currScreen == Screen.SERVER_CONNECTION_MENU || currScreen == Screen.MAIN_MENU) {
                var currState = lastGameUpdate.currentState();
                if (currState == Game.State.STARTED || currState == Game.State.PAUSED) {
                    navigationManager.clearBackStack();
                    navigationManager.navigateTo(Screen.GAME, false);
                }
            }
        }

    }

    @Override
    public void onDisconnect() {
        //tryReconnection();

        communicator = null;
        navigationManager.clearBackStack();
        navigationManager.navigateTo(Screen.SERVER_CONNECTION_MENU, false);
    }

    /**
     * Connect to the server at the given host and port, and call the successListener if the connection is successful, or
     * the errorListener if the connection fails
     *
     * @param host            the hostname of the server to connect to (not parsed, raw input).
     * @param port            the port to connect to (not parsed, raw input).
     * @param successListener a Runnable that will be executed if the connection is successful.
     * @param errorListener   an error listener that will be called if an error occurs.
     */
    public void connect(String host, String port, Runnable successListener, Consumer<Throwable> errorListener) {
        var newHost = host;
        if (host.isBlank())
            newHost = Constants.DEFAULT_HOSTNAME;

        var newPort = Constants.DEFAULT_SERVER_PORT;

        //if a port is defined try to parse it
        if (!port.isBlank())
            try {
                newPort = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                errorListener.accept(new IllegalArgumentException("Inserisci una porta valida"));
                return;
            }

        try {
            communicator = new ClientServerCommunicator(new Socket(newHost, newPort), this);
            communicator.startListening();

            successListener.run();
        } catch (IOException e) {
            errorListener.accept(new Error("Impossibile connettersi al server"));
        }
    }


    public void registerNickname(String nickname, Consumer<Throwable> errorListener) {
        final var newNickname = nickname.isBlank() ? Utils.generateRandomNickname() : nickname;

        communicator.send(new RegisterNicknameRequest(newNickname),
                r -> {
                    if (r.isSuccessful()) {
                        navigationManager.navigateTo(Screen.MAIN_MENU);
                        this.nickname = newNickname;
                    } else
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

    public void leaveGame() {
        communicator.send(
                new LeaveGameRequest(),
                r -> {
                },
                e -> {
                }
        );
        //don't care about the result of the request
        navigationManager.clearBackStack();
        navigationManager.navigateTo(Screen.MAIN_MENU, false);
        lastGameUpdate = null;
    }

    public void addGameUpdateListener(GameUpdateListener listener) {
        gameUpdateListeners.add(listener);
        if (lastGameUpdate != null)
            listener.onGameUpdate(lastGameUpdate);
    }

    public void removeGameUpdateListener(GameUpdateListener listener) {
        gameUpdateListeners.remove(listener);
    }

    public void goToGame() {
        navigationManager.clearBackStack();
        navigationManager.navigateTo(Screen.GAME, false);
    }

    public void getGameList(Consumer<List<GameListItem>> listener, Consumer<Throwable> errorListener) {
        communicator.send(
                new ListGamesRequest(),
                r -> {
                    if (r.isSuccessful())
                        listener.accept(((GamesListReply) r).getGamesList());
                    else
                        errorListener.accept(r.getThrowable());
                },
                errorListener::accept
        );
    }

    public void forwardGameRequest(GameRequest request, Runnable successListener, Consumer<Throwable> errorListener) {
        communicator.send(request, r -> {
                    if (r.isSuccessful())
                        successListener.run();
                    else
                        errorListener.accept(r.getThrowable());
                },
                errorListener::accept
        );
    }

    public String getNickname() {
        return nickname;
    }

    public interface GameUpdateListener {
        void onGameUpdate(ReducedGame game);
    }
}
