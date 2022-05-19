package it.polimi.ingsw.common.responses;

import java.util.UUID;

/**
 * This class represents a Reply, in other words a message from the server to the client
 * associated to a specific request made by the client.
 */
public abstract class Reply extends Response {

    /**
     * Whether the associated request was successful or not.
     */
    private final boolean isSuccessful;

    /**
     * If the associated request is not successful, this field may contain the reason why it failed.
     */
    private Throwable throwable;

    /**
     * The id of the request that generated this reply.
     */
    private final UUID associatedRequestId;

    /**
     * Create a reply to the specified request with the specified success status.
     * @param associatedRequestId
     * @param isSuccessful
     */
    protected Reply(UUID associatedRequestId, boolean isSuccessful) {
        this.associatedRequestId = associatedRequestId;
        this.isSuccessful = isSuccessful;
    }

    /**
     * Create a reply to the specified request which failed because of the specified error.
     */
    protected Reply(UUID associatedRequestId, Throwable throwable) {
        this(associatedRequestId, false);
        this.throwable = throwable;
    }

    /**
     * @return whether the associated request was successful or not.
     */
    public boolean isSuccessful() {
        return isSuccessful;
    }

    /**
     * @return the reason why the associated request failed, if it failed.
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * @return the id of the request that generated this reply.
     */
    public UUID getAssociatedRequestId() {
        return associatedRequestId;
    }
}
