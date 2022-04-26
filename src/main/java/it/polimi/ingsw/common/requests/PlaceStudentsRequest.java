package it.polimi.ingsw.common.requests;

import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.Map;

public class PlaceStudentsRequest extends GameRequest{

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

    public StudentsContainer getInSchool() {
        return inSchool;
    }

    public Map<Integer, StudentsContainer> getInIslands() {
        return inIslands;
    }
}
