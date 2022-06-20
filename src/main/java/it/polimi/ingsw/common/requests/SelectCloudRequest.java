package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.io.Serial;

/**
 * This class represents the request to select a cloud in the currently played game
 */
public class SelectCloudRequest extends GameRequest{

    @Serial
    private static final long serialVersionUID = 6523111696135802221L;

    private final StudentsContainer cloud;

    public SelectCloudRequest(StudentsContainer cloud) {
        this.cloud = cloud;
    }

    @Override
    public Reply handleGameRequest(VirtualView vw, GameController gc) {
        gc.selectCloud(cloud);
        return new AckReply(getRequestId());
    }
}
