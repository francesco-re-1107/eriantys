package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;

import java.io.Serial;

/**
 * This class represents the request to move mother nature in the currently played game.
 */
public class MoveMotherNatureRequest extends GameRequest{

    @Serial
    private static final long serialVersionUID = -3748904840074711985L;

    private final int steps;

    public MoveMotherNatureRequest(int steps) {
        this.steps = steps;
    }

    @Override
    public Reply handleGameRequest(VirtualView vw, GameController gc) {
        gc.moveMotherNature(steps);
        return new AckReply(getRequestId());
    }
}
