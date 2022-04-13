package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;

/**
 * This record represents an Island for the client.
 * This is a reduced version of the Island class, so it used in the communication with the client
 */
public record ReducedIsland(
        StudentsContainer students,
        int size,
        int towersCount,
        Tower towerColor,
        boolean noEntry
) {

    /**
     * Create a ReducedIsland starting from an Island
     * @param i the island to translate
     * @return the ReducedIsland just created
     */
    public static ReducedIsland fromIsland(Island i) {
        return new ReducedIsland(
                i.getStudents(),
                i.getSize(),
                i.getTowersCount(),
                i.getTowerColor(),
                i.isNoEntry()
        );
    }
}
