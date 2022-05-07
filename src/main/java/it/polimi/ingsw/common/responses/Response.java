package it.polimi.ingsw.common.responses;

import java.io.Serializable;

/**
 * This class represents a generic Response from the server to the client
 */
public abstract class Response implements Serializable {

    private final boolean isSuccessful;

    private Throwable throwable;

    public Response(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public Response(Throwable throwable) {
        this(false);
        this.throwable = throwable;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
