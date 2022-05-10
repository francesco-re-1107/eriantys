package it.polimi.ingsw.common.requests;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class represents a generic Request from the client to the server
 */
public abstract class Request implements Serializable {
    /**
     * The unique identifier of the request, generated randomly
     */
    private final UUID requestId;

    public Request() {
        this.requestId = UUID.randomUUID();
    }

    public UUID getId() {
        return requestId;
    }
}
