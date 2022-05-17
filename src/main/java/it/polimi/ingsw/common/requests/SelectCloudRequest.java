package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.StudentsContainer;

/**
 * This class represents the request to select a cloud in the currently played game
 */
public class SelectCloudRequest extends GameRequest{

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
