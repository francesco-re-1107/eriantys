package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;

import java.io.Serial;

/**
 * This class represents the request to leave the currently played game.
 */
public class LeaveGameRequest extends GameRequest{

    @Serial
    private static final long serialVersionUID = 4520437462574775513L;

    @Override
    public Reply handleGameRequest(VirtualView vw, GameController gc) {
        //remove game controller from virtual view
        vw.setGameController(null);

        gc.leaveGame();

        return new AckReply(getRequestId());
    }

}
