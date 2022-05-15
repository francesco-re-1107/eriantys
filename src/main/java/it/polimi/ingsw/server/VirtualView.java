package it.polimi.ingsw.server;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.common.requests.*;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.common.responses.replies.GamesListReply;
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
        var rId = request.getId();
        try {
            if (request instanceof RegisterNicknameRequest r) {
                if(isRegistered)
                    throw new InvalidOperationException("Client already registered");
                setGameController(controller.registerNickname(r.getNickname(), this));
                isRegistered = true;
                nickname = r.getNickname();
                communicator.send(new AckReply(rId));
            } else if (request instanceof ListGamesRequest) {
                if(!isRegistered)
                    throw new InvalidOperationException("Client not registered");
                communicator.send(new GamesListReply(rId, controller.listGames()));
            } else if (request instanceof JoinGameRequest r) {
                if(isInGame)
                    throw new InvalidOperationException("Client already in game");
                //game joined -> new game controller
                setGameController(controller.joinGame(nickname, r.getUUID()));
                communicator.send(new AckReply(rId));
            } else if (request instanceof CreateGameRequest r) {
                if(isInGame)
                    throw new InvalidOperationException("Client already in game");
                //game created -> new game controller
                setGameController(controller.createGame(nickname, r.getNumberOfPlayers(), r.isExpertMode()));
                communicator.send(new AckReply(rId));
            } else if (request instanceof GameRequest r) {
                if(!isInGame)
                    throw new InvalidOperationException("Client not in game");
                processGameRequest(r);
            }
        } catch (Exception | Error e) {
            communicator.send(new NackReply(rId, e));
        }
    }

    private void setGameController(GameController gc) {
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
            setGameController(null);
        }
        //if no exception is thrown send an ack
        communicator.send(new AckReply(request.getId()));
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
