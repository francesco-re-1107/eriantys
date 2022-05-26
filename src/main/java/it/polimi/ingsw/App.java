package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.server.Server;
import javafx.application.Application;

import java.util.logging.Level;

public class App {

    /**
     * Whether to start the client or the server
     */
    private static boolean client;

    /**
     * Whether to start the GUI or the CLI (only if client)
     */
    private static boolean gui;

    public static void main(String[] args) {
        parseArguments(args);

        if (client) {
            if (gui) {
                Utils.LOGGER.info("Starting client with GUI");
                Application.launch(GUI.class);
            } else {
                Utils.LOGGER.setLevel(Level.SEVERE);
                CLI.start();
            }
        } else {
            new Server(Utils.getServerConfig().port())
                    .startListening();
        }
    }

    /**
     * Parses the arguments passed to the program and sets the corresponding flags
     * @param args the arguments passed to the program
     */
    private static void parseArguments(String[] args) {
        try {
            if(args[0].equalsIgnoreCase("client"))
                client = true;
            else if(args[0].equalsIgnoreCase("server"))
                client = false;
            else
                throw new IllegalArgumentException("First argument must be either 'client' or 'server'");

            if(client) {
                if (args[1].equalsIgnoreCase("--cli"))
                    gui = false;
                else if (args[1].equalsIgnoreCase("--gui"))
                    gui = true;
                else
                    throw new IllegalArgumentException("Second argument must be either '--cli' or '--gui'");
            }
        } catch (IllegalArgumentException e) {
            Utils.LOGGER.severe(e.getMessage());
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            Utils.LOGGER.severe("Missing arguments");
            System.exit(0);
        }
    }
}
