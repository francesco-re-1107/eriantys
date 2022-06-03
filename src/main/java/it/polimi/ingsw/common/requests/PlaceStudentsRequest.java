package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.common.responses.Reply;
import it.polimi.ingsw.common.responses.replies.AckReply;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.io.Serial;
import java.util.Map;

/**
 * This class represents the request to place students in school or on islands in the currently played game
 */
public class PlaceStudentsRequest extends GameRequest{

    @Serial
    private static final long serialVersionUID = 3229348692306254734L;

    private final StudentsContainer inSchool;

    /**
     * Students to put on islands
     * The key is the index of the island relative to islands list
     */
    private final Map<Integer, StudentsContainer> inIslands;

    public PlaceStudentsRequest(StudentsContainer inSchool, Map<Integer, StudentsContainer> inIslands) {
        this.inSchool = inSchool;
        this.inIslands = inIslands;
    }

    @Override
    public Reply handleGameRequest(VirtualView vw, GameController gc) {
        gc.placeStudents(inSchool, inIslands);
        return new AckReply(getRequestId());
    }
}
