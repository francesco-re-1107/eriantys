package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

/**
 * This class represents the request to create a game on the server with the specified parameters and automatically
 * join it.
 */
public class CreateGameRequest extends Request{

    private final int numberOfPlayers;

    private final boolean expertMode;

    public CreateGameRequest(int numberOfPlayers, boolean expertMode) {
        this.numberOfPlayers = numberOfPlayers;
        this.expertMode = expertMode;
    }

    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) {
        if(gc != null) //already in game
            throw new InvalidOperationException("Client already in game");
        //game created -> new game controller
        vw.setGameController(c.createGame(c.findNicknameByVirtualView(vw), numberOfPlayers, expertMode));
        return new AckReply(getRequestId());
    }
}
