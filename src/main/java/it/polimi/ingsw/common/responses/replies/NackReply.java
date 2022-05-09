package it.polimi.ingsw.common.responses.replies;

import it.polimi.ingsw.common.responses.Reply;

import java.util.UUID;

public class NackReply extends Reply {

    public NackReply(UUID requestId, Throwable e) {
        super(requestId, e);
    }

}
