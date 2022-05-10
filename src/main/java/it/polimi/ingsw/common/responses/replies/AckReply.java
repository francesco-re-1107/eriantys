package it.polimi.ingsw.common.responses.replies;

import it.polimi.ingsw.common.responses.Reply;

import java.util.UUID;

/**
 * This class is used to send an ACK message to the client. In other words, it is used as an empty response
 * just to notify the client that the request has been correctly received and successfully processed.
 */
public class AckReply extends Reply {

    public AckReply(UUID requestId) {
        super(requestId, true);
    }
}
