package it.polimi.ingsw.server;

import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.requests.Request;
import it.polimi.ingsw.common.responses.replies.NackReply;
import it.polimi.ingsw.common.responses.updates.GameUpdate;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.Game;

import java.net.Socket;

/**
 * This class models a virtual view, this class communicates with the client (through a ServerClientCommunicator)
 * and forward every Request to controller and the game controller. The Response(s) are transmitted back to the client.
 */
public class VirtualView implements ServerClientCommunicator.CommunicatorListener,
        GameController.GameUpdateListener, Runnable {

    /**
     * Used for communication
     */
    private final ServerClientCommunicator communicator;

    /**
     * Reference to the main controller of the server
     */
    private final Controller controller;

    /**
     * Reference to the controller for the current game
     * If null then there's no game currently played (TODO: Maybe use an optional)
     */
    private GameController gameController;

    /**
     * Whether the nickname is already registered
     */
    private boolean isRegistered = false;

    /**
     * Stores the nickname the player registered with
     */
    private String nickname = "";

    /**
     * Whether the player is currently in a game
     */
    private boolean isInGame = false;

    /**
     * Create a VirtualView
     * @param controller the instance of the main controller
     * @param socket the client socket accepted by the server
     */
    public VirtualView(Controller controller, Socket socket) {
        this.communicator = new ServerClientCommunicator(socket, this);
        this.controller = controller;
    }

    /**
     * This virtual view is executed in a new thread.
     * The first thing to do is listening on the socket
     */
    @Override
    public void run() {
        this.communicator.startListening();
    }

    /**
     * Callback for every request received from the client
     * @param request
     */
    @Override
    public void onRequest(Request request) {
        processRequest(request);
    }

    /**
     * This method handles every request (used internally).
     * GameRequest(s) are forwarded to the processGameRequest method.
     * @param request the request to handle
     */
    private void processRequest(Request request) {
        var rId = request.getRequestId();
        try {
            communicator.send(request.handleRequest(this, controller, gameController));
        } catch (Exception | Error e) {
            communicator.send(new NackReply(rId, e));
        }
    }

    public void setGameController(GameController gc) {
        if(gc == null){
            gameController = null;
            isInGame = false;
        } else {
            gameController = gc;
            gameController.setOnGameUpdateListener(this);
            isInGame = true;
        }
    }

    /**
     * This callback is called when the socket disconnects.
     * Used to update the internal status of disconnected variable
     */
    @Override
    public void onDisconnect() {
        if(gameController != null)
            gameController.disconnect();
    }

    /**
     * Whether the client associated with this virtual view disconnected
     * @return true if disconnected, false otherwise
     */
    public boolean isConnected() {
        return communicator.isConnected();
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    /**
     * Callback from the controller.
     * For every game update, the game is wrapped in a GameUpdateResponse and sent to the client
     * @param game
     */
    @Override
    public void onGameUpdate(ReducedGame game) {
        communicator.send(new GameUpdate(game));

        var state = game.currentState();

        //game ended
        if(state == Game.State.TERMINATED || state == Game.State.FINISHED) {
            setGameController(null);
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        isRegistered = true;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public String getNickname() {
        return nickname;
    }
}
