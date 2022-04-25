package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;

import java.util.Collections;

public class CLI {
    public static void Main(){
        var c = new Cursor();
        var s = new StudentsContainer();
        s.addStudents(Student.YELLOW, 2);
        s.addStudents(Student.BLUE, 1);
        s.addStudents(Student.PINK, 3);
        var i = new ReducedIsland(s, 1, 3, Tower.WHITE, false);
        var islands = Collections.nCopies(12, i);
        c.drawIslands(islands);
    }
}
