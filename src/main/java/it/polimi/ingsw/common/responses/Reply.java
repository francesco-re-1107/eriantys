package it.polimi.ingsw.common.responses;

import java.util.UUID;

/**
 * This class represents a generic Response from the server to the client
 */
public abstract class Reply extends Response {

    private final boolean isSuccessful;

    private Throwable throwable;

    private final UUID requestId;

    public Reply(UUID requestId, boolean isSuccessful) {
        this.requestId = requestId;
        this.isSuccessful = isSuccessful;
    }

    public Reply(UUID requestId, Throwable throwable) {
        this(requestId, false);
        this.throwable = throwable;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public UUID getRequestId() {
        return requestId;
    }
}
