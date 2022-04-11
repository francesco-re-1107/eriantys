package it.polimi.ingsw.reducedmodel;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.StudentsContainer;
import it.polimi.ingsw.model.Tower;

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
