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

import java.net.Socket;

public class VirtualView implements ServerClientCommunicator.CommunicatorListener,
        GameController.GameUpdateListener, Runnable {

    private final ServerClientCommunicator communicator;

    private final Controller controller;

    private GameController gameController;

    private boolean isConnected = false;

    private String nickname = "";

    public VirtualView(Controller controller, Socket socket) {
        this.communicator = new ServerClientCommunicator(socket, this);
        this.controller = controller;
    }

    @Override
    public void onRequest(Request request) {
        processRequest(request);
    }

    private void processRequest(Request request) {
        try {
            if (request instanceof RegisterNicknameRequest r) {
                controller.registerNickname(r.getNickname(), this);
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
                processGameRequest(request);
            }
        } catch (Exception | Error e) {
            communicator.send(new ErrorResponse(e));
        }
    }

    private void processGameRequest(Request request) {
        if (gameController == null)
            throw new InvalidOperationException("Cannot process game requests before a game is joined");

        if (request instanceof PlayAssistantCardRequest r) {
            gameController.playAssistantCard(r.getCard());
        } else if (request instanceof  r) {

        } else if (request instanceof  r) {

        } else if (request instanceof  r) {

        }
        //if no exception is thrown send an ack
        communicator.send(new AckResponse());
    }

    @Override
    public void onDisconnect() {
        this.isConnected = true;
        this.gameController.disconnect();
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onGameUpdate(ReducedGame game) {
        communicator.send(new GameUpdateResponse(game));
    }

    @Override
    public void run() {
        this.communicator.startListening();
    }
}
