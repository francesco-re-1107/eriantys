package it.polimi.ingsw.server;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.requests.*;
import it.polimi.ingsw.common.responses.AckResponse;
import it.polimi.ingsw.common.responses.ErrorResponse;
import it.polimi.ingsw.common.responses.GameUpdateResponse;
import it.polimi.ingsw.common.responses.GamesListResponse;
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
     * Whether the client disconnected from the view
     */
    private boolean isConnected = true;

    /**
     * Stores the nickname the player registered with
     */
    private String nickname = "";

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
        try {
            if (request instanceof RegisterNicknameRequest r) {
                this.gameController = controller.registerNickname(r.getNickname(), this);
                this.nickname = r.getNickname();
                communicator.send(new AckResponse());
            } else if (request instanceof ListGamesRequest) {
                communicator.send(new GamesListResponse(controller.listGames()));
            } else if (request instanceof JoinGameRequest r) {
                //game joined -> new game controller
                gameController = controller.joinGame(nickname, r.getUUID());
                gameController.setOnGameUpdateListener(this);
                communicator.send(new AckResponse());
            } else if (request instanceof CreateGameRequest r) {
                //game created -> new game controller
                gameController = controller.createGame(nickname, r.getNumberOfPlayers(), r.isExpertMode());
                gameController.setOnGameUpdateListener(this);
                communicator.send(new AckResponse());
            } else if (request instanceof GameRequest r) {
                processGameRequest(r);
            }
        } catch (Exception | Error e) {
            communicator.send(new ErrorResponse(e));
        }
    }

    /**
     * This method handles all the game requests (used internally)
     * @param request the game request to handle
     */
    private void processGameRequest(GameRequest request) {
        if (gameController == null)
            throw new InvalidOperationException("Cannot process game requests before a game is joined");

        if (request instanceof PlayAssistantCardRequest r) {
            gameController.playAssistantCard(r.getCard());
        } else if (request instanceof PlaceStudentsRequest r) {
            gameController.placeStudents(r.getInSchool(), r.getInIslands());
        } else if (request instanceof PlayCharacterCardRequest r) {
            gameController.playCharacterCard(r.getCharacterCard());
        } else if (request instanceof MoveMotherNatureRequest r) {
            gameController.moveMotherNature(r.getSteps());
        } else if (request instanceof SelectCloudRequest r) {
            gameController.selectCloud(r.getCloud());
        } else if (request instanceof LeaveGameRequest) {
            gameController.leaveGame();
        }
        //if no exception is thrown send an ack
        communicator.send(new AckResponse());
    }

    /**
     * This callback is called when the socket disconnects.
     * Used to update the internal status of disconnected variable
     */
    @Override
    public void onDisconnect() {
        this.isConnected = false;

        if(gameController != null)
            this.gameController.disconnect();
    }

    /**
     * Whether the client associated with this virtual view disconnected
     * @return true if disconnected, false otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Callback from the controller.
     * For every game update, the game is wrapped in a GameUpdateResponse and sent to the client
     * @param game
     */
    @Override
    public void onGameUpdate(ReducedGame game) {
        communicator.send(new GameUpdateResponse(game));

        Game.State state = game.currentState();

        //if game's finished are removed from the list
        if(state == Game.State.TERMINATED || state == Game.State.FINISHED)
            this.gameController = null; //this game was finished
    }
}
