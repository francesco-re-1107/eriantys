package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.GameController;

import java.io.Serial;

/**
 * This request is used to ping the server every @see Constants#PING_INTERVAL milliseconds.
 */
public class PingRequest extends Request {

    @Serial
    private static final long serialVersionUID = -9057242002941021048L;

    @Override
    public Reply handleRequest(VirtualView vw, Controller c, GameController gc) {
        return new AckReply(getRequestId());
    }
}
