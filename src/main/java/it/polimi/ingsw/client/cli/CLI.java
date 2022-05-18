package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.common.reducedmodel.ReducedRound;
import it.polimi.ingsw.common.reducedmodel.ReducedPlayer;
import it.polimi.ingsw.common.reducedmodel.ReducedGame;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;

import java.util.Collections;
import java.io.IOException;
import java.util.*;

public class CLI {

    public static void main(String[] args){
        //var cfg = Utils.GetAppConfig();
        Client.init();
    }

    public static void testDrawBoard() {
        var c = Cursor.getInstance();
        var s = new StudentsContainer();
        s.addStudents(Student.YELLOW, 2);
        s.addStudents(Student.BLUE, 1);
        s.addStudents(Student.PINK, 3);
        var i = new ReducedIsland(s, 1, 3, Tower.WHITE, false);
        var islands = Collections.nCopies(12, i);
        c.drawIslands(islands);
        var p1 = new ReducedPlayer(
                "p1",
                true,
                new StudentsContainer()
                        .addStudents(Student.BLUE, 3)
                        .addStudents(Student.GREEN, 1)
                        .addStudents(Student.RED, 1)
                        .addStudents(Student.YELLOW, 1)
                        .addStudents(Student.PINK, 3),
                new StudentsContainer()
                        .addStudents(Student.BLUE, 1)
                        .addStudents(Student.GREEN, 3)
                        .addStudents(Student.RED, 4)
                        .addStudents(Student.YELLOW, 2)
                        .addStudents(Student.PINK, 1),
                3,
                Tower.WHITE,
                null,
                3);

        var p2 = new ReducedPlayer(
                "p2",
                true,
                new StudentsContainer()
                        .addStudents(Student.BLUE, 2)
                        .addStudents(Student.GREEN, 5)
                        .addStudents(Student.RED, 3)
                        .addStudents(Student.YELLOW, 2)
                        .addStudents(Student.PINK, 1),
                new StudentsContainer()
                        .addStudents(Student.BLUE, 1)
                        .addStudents(Student.GREEN, 4)
                        .addStudents(Student.RED, 2)
                        .addStudents(Student.YELLOW, 1)
                        .addStudents(Student.PINK, 3),
                5,
                Tower.BLACK,
                null,
                4);

        ReducedRound r = new ReducedRound(null, p1, null, 0);
        var players = List.of(p1, p2);
        var prof = new HashMap<Student, ReducedPlayer>();
        prof.put(Student.YELLOW, p1);
        prof.put(Student.GREEN, p2);
        prof.put(Student.RED, p1);
        var g = new ReducedGame(null, 2, false, players, null, 0, 5, r, null, prof, null, null);
        c.drawBoard(players, r, g);
        c.printPrompt("Comando", "...");
        var n = new Scanner(System.in).nextLine();
    }
}
