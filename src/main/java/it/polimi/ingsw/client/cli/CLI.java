package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;

import java.io.IOException;
import java.util.Collections;

public class CLI {
    public static void main(String[] args){
        var cfg = Utils.GetAppConfig();
        try {
            Client client = new Client(cfg.server_url(), cfg.port());

        } catch (IOException e) {
            e.printStackTrace();
            Utils.LOGGER.severe(e.getMessage());
        }
    }

    public static void testDrawBoard(){
        var c = Cursor.getInstance();
        var s = new StudentsContainer();
        s.addStudents(Student.YELLOW, 2);
        s.addStudents(Student.BLUE, 1);
        s.addStudents(Student.PINK, 3);
        var i = new ReducedIsland(s, 1, 3, Tower.WHITE, false);
        var islands = Collections.nCopies(12, i);
        c.drawIslands(islands);
    }
}
