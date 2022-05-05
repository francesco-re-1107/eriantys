package it.polimi.ingsw;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.server.Server;
import javafx.application.Application;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        var client = false;
        var gui = false;

        //rough arguments parsing to determine if server or client must be started
        try {
            client = args[0].equals("client");

            if (client)
                gui = args[1].equals("--gui");

        } catch (Exception e) {
            Utils.LOGGER.severe("Arguments error");
            System.exit(0);
        }

        if (client) {
            startClient(gui);
        } else {
            startServer();
        }
    }

    /**
     * Start the server
     */
    public static void startServer() {
        new Server(readPortFromProperties())
                .startListening();
    }

    /**
     * Start the client with cli or gui respectively
     *
     * @param gui whether the client must be started with gui or not.
     */
    public static void startClient(boolean gui) {
        if (gui) {
            Utils.LOGGER.info("Starting client with GUI");
            Application.launch(GUI.class);
        } else {
            Utils.LOGGER.info("Starting CLI client");
        }
    }

    /**
     * Read server port from properties if the config file exists otherwise the DEFAULT_SERVER_PORT is used
     *
     * @return the server port to use
     */
    private static int readPortFromProperties() {
        Properties prop = new Properties();

        try (FileInputStream fis = new FileInputStream(Constants.CONFIG_FILE_PATH)) {
            prop.load(fis);

            return Integer.parseInt(prop.getProperty("server.port"));
        } catch (Exception e) {
            return Constants.DEFAULT_SERVER_PORT;
        }

    }
}
