package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * This class represents a generic Request from the client to the server
 */
public abstract class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = -6173046168193803850L;

    /**
     * The unique identifier of the request, generated randomly
     */
    private final UUID requestId;

    protected Request() {
        this.requestId = UUID.randomUUID();
    }

    public UUID getRequestId() {
        return requestId;
    }

    /**
     * This method is called by the server to execute the request
     *
     * @param vw the virtual view of the client
     * @param c the controller of the game
     * @param gc the game controller
     * @return the reply of the request
     */
    public abstract Reply handleRequest(VirtualView vw, Controller c, GameController gc) throws Exception;
}
