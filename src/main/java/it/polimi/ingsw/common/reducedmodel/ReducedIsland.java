package it.polimi.ingsw.common.reducedmodel;

import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;

public record ReducedIsland(
        StudentsContainer students,
        int size,
        int towersCount,
        Tower towerColor,
        boolean noEntry
) {

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
