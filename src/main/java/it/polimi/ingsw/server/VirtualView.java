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
     * If null then there's no game currently played
     */
    private GameController gameController;

    /**
     * Registered nickname, null if the client is not yet registered
     */
    private String nickname;

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
     * This method handles every request.
     * @param request the request to handle
     */
    private void processRequest(Request request) {
        var rId = request.getRequestId();
        try {
            communicator.send(request.handleRequest(this, controller, gameController));
        } catch (Exception | Error e) {
            e.printStackTrace();
            communicator.send(new NackReply(rId, e));
        }
    }

    /**
     * Update the current game controller to the given one
     * @param gc the new game controller, null if there's no game currently played
     */
    public void setGameController(GameController gc) {
        if(gc == null){
            gameController = null;
        } else {
            gameController = gc;
            gameController.setOnGameUpdateListener(this);
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

        if(nickname != null)
            controller.disconnect(nickname);
    }

    /**
     * Set registered nickname
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the registered nickname
     */
    public String getNickname() {
        return nickname;
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

}
