package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.exceptions.InvalidOperationError;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

import java.io.Serial;

/**
 * This class represents a generic request regarding a specific game.
 */
public abstract class GameRequest extends Request{

    @Serial
    private static final long serialVersionUID = 2723707173376379269L;

    public abstract Reply handleGameRequest(VirtualView vw, GameController gameController);

    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) throws Exception {
        if(gc == null) { //client is not currently playing any game
            if (this instanceof LeaveGameRequest) //if the request is to leave a game, then it's ok
                return new AckReply(getRequestId());
            else //for any other game requests, client must be in game
                throw new InvalidOperationError("Client not in game");
        }

        return handleGameRequest(vw, gc);
    }
}
