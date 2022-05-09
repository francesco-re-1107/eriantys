package it.polimi.ingsw.common.responses.replies;

import it.polimi.ingsw.common.responses.Reply;

import java.util.UUID;

public class AckReply extends Reply {

    public AckReply(UUID requestId) {
        super(requestId, true);
    }
}
