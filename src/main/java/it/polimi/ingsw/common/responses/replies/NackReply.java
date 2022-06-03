package it.polimi.ingsw.common.responses.replies;

import it.polimi.ingsw.common.responses.Reply;

import java.io.Serial;
import java.util.UUID;

/**
 * This class is used to send a NACK message to the client. In other words, it is used as an empty response
 * just to notify the client that the request has been correctly received, but it wasn't successful.
 */
public class NackReply extends Reply {

    @Serial
    private static final long serialVersionUID = 9113815591700132240L;

    public NackReply(UUID requestId, Throwable e) {
        super(requestId, e);
    }

}
