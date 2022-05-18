package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

import java.util.UUID;

/**
 * This class represents the request to join the game with the specified uuid
 */
public class JoinGameRequest extends Request{

    private final UUID gameId;

    public JoinGameRequest(UUID gameId) {
        this.gameId = gameId;
    }

    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) {
        if(gc != null)
            throw new InvalidOperationException("Client already in game");

        //game joined -> new game controller
        vw.setGameController(c.joinGame(vw.getNickname(), gameId));
        return new AckReply(getRequestId());
    }
}
