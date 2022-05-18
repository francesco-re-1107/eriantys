package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;

/**
 * This class represents the request to leave the currently played game.
 */
public class LeaveGameRequest extends GameRequest{

    @Override
    public Reply handleGameRequest(VirtualView vw, GameController gc) {
        gc.leaveGame();
        return new AckReply(getRequestId());
    }

}
