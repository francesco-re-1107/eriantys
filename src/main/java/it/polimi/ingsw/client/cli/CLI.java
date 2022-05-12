package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.Utils;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.common.reducedmodel.ReducedIsland;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;
import it.polimi.ingsw.server.model.Tower;

import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

public class CLI {

    public static void main(String[] args){
        var cfg = Utils.GetAppConfig();
        try {
            Client client = new Client(cfg.server_url(), cfg.port());

            do {
                var n = new Scanner(System.in).nextLine();
                client.registerNickname("CLI" + n, r -> {
                    if (r.isSuccessful()) {
                        System.out.println("Registered successfully");
                    } else {
                        System.out.println("Registration failed");
                        r.getThrowable().printStackTrace();
                    }
                }, e -> {
                    System.out.println("Registration failed");
                    System.out.println(e.getMessage());
                });

            } while (true);
        } catch (IOException e) {
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
