package it.polimi.ingsw.common.responses;

import java.io.Serial;
import java.io.Serializable;


/**
 * A Response is a message from server to client.
 */
public abstract class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 416132178283992882L;
}
