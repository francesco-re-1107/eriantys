package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.exceptions.InvalidOperationException;
import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

/**
 * This class represents a generic request regarding a specific game.
 */
public abstract class GameRequest extends Request{

    public abstract Reply handleGameRequest(VirtualView vw, GameController gameController);

    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) throws Exception {
        if(gc == null)
            throw new InvalidOperationException("Client not in game");

        return handleGameRequest(vw, gc);
    }
}
